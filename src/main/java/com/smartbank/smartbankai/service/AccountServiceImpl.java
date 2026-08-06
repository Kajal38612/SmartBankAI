package com.smartbank.smartbankai.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartbank.smartbankai.dto.AccountRequest;
import com.smartbank.smartbankai.entity.Account;
import com.smartbank.smartbankai.entity.User;
import com.smartbank.smartbankai.exception.UserNotFoundException;
import com.smartbank.smartbankai.repository.AccountRepository;
import com.smartbank.smartbankai.repository.UserRepository;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Account createAccount(AccountRequest request, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        Account account = new Account();

        account.setAccountType(request.getAccountType());
        account.setBalance(request.getBalance());
        account.setStatus(request.getStatus());

        account.setCreatedAt(LocalDateTime.now());

        account.setAccountNumber("SB" + System.currentTimeMillis());

        account.setUser(user);

        return accountRepository.save(account);
    }

    @Override
    public List<Account> getAllAccounts() {

        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> getAccountById(Long id) {

        return accountRepository.findById(id);
    }

    @Override
    public Optional<Account> updateAccount(Long id, Account account) {

        Optional<Account> existingAccount = accountRepository.findById(id);

        if (existingAccount.isPresent()) {

            Account updatedAccount = existingAccount.get();
            if ("CLOSED".equals(updatedAccount.getStatus())) {
                throw new RuntimeException("Closed account cannot be updated.");
            }

            // Account Number ko update nahi karenge

            updatedAccount.setAccountType(account.getAccountType());
            updatedAccount.setBalance(account.getBalance());
            updatedAccount.setStatus(account.getStatus());

            // createdAt ko bhi update nahi karenge

            return Optional.of(accountRepository.save(updatedAccount));
        }

        return Optional.empty();
    }

    @Override
    public Optional<Account> closeAccount(Long id) {

        Optional<Account> existingAccount = accountRepository.findById(id);

        if (existingAccount.isPresent()) {

            Account account = existingAccount.get();

            account.setStatus("CLOSED");

            return Optional.of(accountRepository.save(account));
        }

        return Optional.empty();
    }
}