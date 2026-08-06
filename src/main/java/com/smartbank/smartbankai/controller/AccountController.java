package com.smartbank.smartbankai.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartbank.smartbankai.dto.AccountRequest;
import com.smartbank.smartbankai.entity.Account;
import com.smartbank.smartbankai.entity.User;
import com.smartbank.smartbankai.repository.UserRepository;
import com.smartbank.smartbankai.security.JwtService;
import com.smartbank.smartbankai.service.AccountService;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;
   
    @PostMapping("/create")
    public Account createAccount(
            @RequestBody AccountRequest request,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);

        String email = jwtService.extractUsername(token);

        return accountService.createAccount(request, email);
    }

//    @GetMapping("/all")
//    public List<Account> getAllAccounts() {
//
//        return accountService.getAllAccounts();
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);

        String email = jwtService.extractUsername(token);

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Account> optionalAccount = accountService.getAccountById(id);

        if (optionalAccount.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Account account = optionalAccount.get();

        if (!account.getUser().getId().equals(loggedInUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(account);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Account> updateAccount(
            @PathVariable Long id,
            @RequestBody Account account,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);

        String email = jwtService.extractUsername(token);

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Account> optionalAccount = accountService.getAccountById(id);

        if (optionalAccount.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Account existingAccount = optionalAccount.get();

        if (!existingAccount.getUser().getId().equals(loggedInUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<Account> updatedAccount =
                accountService.updateAccount(id, account);

        return updatedAccount
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/close/{id}")
    public ResponseEntity<String> closeAccount(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);

        String email = jwtService.extractUsername(token);

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Account> optionalAccount = accountService.getAccountById(id);

        if (optionalAccount.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Account account = optionalAccount.get();

        if (!account.getUser().getId().equals(loggedInUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        accountService.closeAccount(id);

        return ResponseEntity.ok("Account closed successfully.");
    }
}