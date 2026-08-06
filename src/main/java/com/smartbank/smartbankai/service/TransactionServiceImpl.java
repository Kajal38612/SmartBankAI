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

    @Override
    public Transaction performTransaction(TransactionRequest request) {

        Optional<Account> optionalAccount =
                accountRepository.findById(request.getAccountId());

        if (optionalAccount.isEmpty()) {
            throw new AccountNotFoundException("Account not found");
        }

        Account account = optionalAccount.get();
        validateAccountOwnership(account);
        if ("CLOSED".equals(account.getStatus())) {
            throw new RuntimeException("Account is closed. Transactions are not allowed.");
        }

        // Deposit
        if (request.getTransactionType() == TransactionType.DEPOSIT) {

            account.setBalance(account.getBalance() + request.getAmount());
        }

        // Withdraw
        else if (request.getTransactionType() == TransactionType.WITHDRAW) {

            if (account.getBalance() < request.getAmount()) {
                throw new InsufficientBalanceException("Insufficient balance");
            }

            account.setBalance(account.getBalance() - request.getAmount());
        }

        // Save updated account balance
        accountRepository.save(account);

        // Create transaction
        Transaction transaction = new Transaction();

        transaction.setTransactionId("TXN" + System.currentTimeMillis());
        transaction.setTransactionType(request.getTransactionType());
        transaction.setAmount(request.getAmount());
        transaction.setBalanceAfterTransaction(account.getBalance());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setRemarks(request.getRemarks());
        transaction.setAccount(account);

        return transactionRepository.save(transaction);
    }
    
    @Transactional
    @Override
    public Transaction transferMoney(TransactionRequest request) {

        Account sender = accountRepository.findById(request.getAccountId())
                .orElseThrow(() ->
                        new AccountNotFoundException("Sender account not found"));
        validateAccountOwnership(sender);

        Account receiver = accountRepository.findById(request.getReceiverAccountId())
                .orElseThrow(() ->
                        new AccountNotFoundException("Receiver account not found"));
        if ("CLOSED".equals(sender.getStatus())) {
            throw new RuntimeException("Sender account is closed.");
        }

        if ("CLOSED".equals(receiver.getStatus())) {
            throw new RuntimeException("Receiver account is closed.");
        }

        if (sender.getId().equals(receiver.getId())) {
            throw new RuntimeException("Sender and Receiver cannot be same");
        }

        if (sender.getBalance() < request.getAmount()) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        // Withdraw from sender
        sender.setBalance(sender.getBalance() - request.getAmount());

        // Deposit to receiver
        receiver.setBalance(receiver.getBalance() + request.getAmount());

        accountRepository.save(sender);
        accountRepository.save(receiver);

        Transaction transaction = new Transaction();

        transaction.setTransactionId("TXN" + System.currentTimeMillis());
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setAmount(request.getAmount());
        transaction.setBalanceAfterTransaction(sender.getBalance());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setRemarks(request.getRemarks());
        transaction.setAccount(sender);

        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionHistory(Long accountId) {

        return transactionRepository.findByAccountId(accountId);
    }
    private void validateAccountOwnership(Account account) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String loggedInEmail = authentication.getName();

        if (!account.getUser().getEmail().equals(loggedInEmail)) {

            throw new UnauthorizedAccessException(
                    "You are not authorized to access this account.");
        }
    }
    
}