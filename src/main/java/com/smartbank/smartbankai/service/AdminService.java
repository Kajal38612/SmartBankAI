package com.smartbank.smartbankai.service;

import java.util.List;

import com.smartbank.smartbankai.dto.AdminLoginRequest;
import com.smartbank.smartbankai.dto.AdminLoginResponse;
import com.smartbank.smartbankai.dto.AdminResponse;
import com.smartbank.smartbankai.entity.Account;
import com.smartbank.smartbankai.entity.Transaction;
import com.smartbank.smartbankai.entity.User;

public interface AdminService {

    AdminLoginResponse login(AdminLoginRequest request);

    List<User> getAllUsers();

    List<Account> getAllAccounts();

    List<Transaction> getAllTransactions();

    Account freezeAccount(Long accountId);

    Account activateAccount(Long accountId);

    Account closeAccount(Long accountId);
    AdminResponse getProfile();

}