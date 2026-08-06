package com.smartbank.smartbankai.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartbank.smartbankai.dto.DashboardResponse;
import com.smartbank.smartbankai.entity.Account;
import com.smartbank.smartbankai.entity.Transaction;
import com.smartbank.smartbankai.exception.AccountNotFoundException;
import com.smartbank.smartbankai.repository.AccountRepository;
import com.smartbank.smartbankai.repository.TransactionRepository;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;
    @Override
    public DashboardResponse getDashboard(Long accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found"));
        

        List<Transaction> transactions =
                transactionRepository.findByAccountId(accountId);

        return new DashboardResponse(
                account.getUser().getFullName(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getBalance(),
                account.getStatus(),
                transactions
        );
    }
}