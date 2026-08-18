package com.smartbank.smartbankai.service;

import java.util.List;
import java.util.Optional;

import com.smartbank.smartbankai.dto.AccountRequest;
import com.smartbank.smartbankai.entity.Account;

public interface AccountService {

    
    

    List<Account> getAllAccounts();

    Optional<Account> getAccountById(Long id);
	Optional<Account> getAccountByUserId(Long userId);
    

    Optional<Account> updateAccount(Long id, Account account);

    
    Optional<Account> closeAccount(Long id);

	Account createAccount(AccountRequest request, String email);

	

}
