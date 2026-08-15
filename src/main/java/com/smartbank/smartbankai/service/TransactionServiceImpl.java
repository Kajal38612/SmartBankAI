package com.smartbank.smartbankai.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartbank.smartbankai.dto.TransactionRequest;
import com.smartbank.smartbankai.entity.Account;
import com.smartbank.smartbankai.entity.Transaction;
import com.smartbank.smartbankai.entity.TransactionType;
import com.smartbank.smartbankai.exception.AccountNotFoundException;
import com.smartbank.smartbankai.exception.InsufficientBalanceException;
import com.smartbank.smartbankai.exception.UnauthorizedAccessException;
import com.smartbank.smartbankai.repository.AccountRepository;
import com.smartbank.smartbankai.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;


    // =====================================================
    // DEPOSIT / WITHDRAW
    // ADMIN ONLY
    // =====================================================

    @Override
    public Transaction performTransaction(TransactionRequest request) {

        Optional<Account> optionalAccount =
                accountRepository.findById(request.getAccountId());

        if (optionalAccount.isEmpty()) {
            throw new AccountNotFoundException("Account not found");
        }

        Account account = optionalAccount.get();

        // /transaction/perform is protected as ADMIN
        // in SecurityConfig, so no USER ownership check
        // is required here.

        if ("CLOSED".equals(account.getStatus())) {
            throw new RuntimeException(
                    "Account is closed. Transactions are not allowed.");
        }

        // =========================
        // DEPOSIT
        // =========================

        if (request.getTransactionType() == TransactionType.DEPOSIT) {

            account.setBalance(
                    account.getBalance() + request.getAmount()
            );
        }

        // =========================
        // WITHDRAW
        // =========================

        else if (request.getTransactionType()
                == TransactionType.WITHDRAW) {

            if (account.getBalance() < request.getAmount()) {

                throw new InsufficientBalanceException(
                        "Insufficient balance");
            }

            account.setBalance(
                    account.getBalance() - request.getAmount()
            );
        }

        // Save updated balance
        accountRepository.save(account);

        // =========================
        // CREATE TRANSACTION
        // =========================

        Transaction transaction = new Transaction();

        transaction.setTransactionId(
                "TXN" + System.currentTimeMillis()
        );

        transaction.setTransactionType(
                request.getTransactionType()
        );

        transaction.setAmount(
                request.getAmount()
        );

        transaction.setBalanceAfterTransaction(
                account.getBalance()
        );

        transaction.setTransactionDate(
                LocalDateTime.now()
        );

        transaction.setRemarks(
                request.getRemarks()
        );

        transaction.setAccount(account);

        return transactionRepository.save(transaction);
    }


    // =====================================================
    // TRANSFER MONEY
    // USER + ADMIN
    // =====================================================

    @Transactional
    @Override
    public Transaction transferMoney(TransactionRequest request) {

        // =========================
        // SENDER
        // =========================

        Account sender = accountRepository
                .findById(request.getAccountId())
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Sender account not found"));

        // USER → own account only
        // ADMIN → any account
        validateAccountOwnership(sender);


        // =========================
        // RECEIVER
        // =========================

        Account receiver = accountRepository
                .findById(request.getReceiverAccountId())
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Receiver account not found"));


        // =========================
        // ACCOUNT STATUS
        // =========================

        if ("CLOSED".equals(sender.getStatus())) {

            throw new RuntimeException(
                    "Sender account is closed.");
        }

        if ("CLOSED".equals(receiver.getStatus())) {

            throw new RuntimeException(
                    "Receiver account is closed.");
        }


        // =========================
        // SAME ACCOUNT CHECK
        // =========================

        if (sender.getId().equals(receiver.getId())) {

            throw new RuntimeException(
                    "Sender and Receiver cannot be same");
        }


        // =========================
        // BALANCE CHECK
        // =========================

        if (sender.getBalance() < request.getAmount()) {

            throw new InsufficientBalanceException(
                    "Insufficient balance");
        }


        // =========================
        // TRANSFER
        // =========================

        // Withdraw from sender
        sender.setBalance(
                sender.getBalance() - request.getAmount()
        );

        // Deposit to receiver
        receiver.setBalance(
                receiver.getBalance() + request.getAmount()
        );


        // Save both accounts
        accountRepository.save(sender);
        accountRepository.save(receiver);


        // =========================
        // CREATE TRANSACTION
        // =========================

        Transaction transaction = new Transaction();

        transaction.setTransactionId(
                "TXN" + System.currentTimeMillis()
        );

        transaction.setTransactionType(
                TransactionType.TRANSFER
        );

        transaction.setAmount(
                request.getAmount()
        );

        transaction.setBalanceAfterTransaction(
                sender.getBalance()
        );

        transaction.setTransactionDate(
                LocalDateTime.now()
        );

        transaction.setRemarks(
                request.getRemarks()
        );

        transaction.setAccount(sender);

        return transactionRepository.save(transaction);
    }


    // =====================================================
    // TRANSACTION HISTORY
    // =====================================================

    @Override
    public List<Transaction> getTransactionHistory(
            Long accountId) {

        return transactionRepository
                .findByAccountId(accountId);
    }


    // =====================================================
    // ACCOUNT OWNERSHIP CHECK
    // USER → OWN ACCOUNT
    // ADMIN → ANY ACCOUNT
    // =====================================================

    private void validateAccountOwnership(
            Account account) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();


        // =========================
        // ADMIN CHECK
        // =========================

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                "ADMIN".equals(
                                        authority.getAuthority()
                                ));


        // ADMIN can access any account
        if (isAdmin) {
            return;
        }


        // =========================
        // USER CHECK
        // =========================

        String loggedInEmail =
                authentication.getName();


        if (!account.getUser()
                .getEmail()
                .equals(loggedInEmail)) {

            throw new UnauthorizedAccessException(
                    "You are not authorized to access this account.");
        }
    }
}
