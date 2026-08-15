package com.smartbank.smartbankai.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartbank.smartbankai.dto.TransactionRequest;
import com.smartbank.smartbankai.entity.Account;
import com.smartbank.smartbankai.entity.Transaction;
import com.smartbank.smartbankai.entity.User;
import com.smartbank.smartbankai.repository.AccountRepository;
import com.smartbank.smartbankai.repository.UserRepository;
import com.smartbank.smartbankai.security.JwtService;
import com.smartbank.smartbankai.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;


    // ==========================================
    // DEPOSIT / WITHDRAW
    // ADMIN ONLY
    // ==========================================

    @PostMapping("/perform")
    public ResponseEntity<Transaction> performTransaction(
            @Valid @RequestBody TransactionRequest request,
            @RequestHeader("Authorization") String authHeader) {

        // SecurityConfig already allows this endpoint
        // only for ADMIN.

        return ResponseEntity.ok(
                transactionService.performTransaction(request)
        );
    }


    // ==========================================
    // TRANSACTION HISTORY
    // USER + ADMIN
    // ==========================================

    @GetMapping("/history/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionHistory(
            @PathVariable Long accountId,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);

        String email = jwtService.extractUsername(token);

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        // Check ADMIN authority
        boolean isAdmin = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(authority ->
                        "ADMIN".equals(authority.getAuthority()));

        // USER can see only own transactions.
        // ADMIN can see any user's transactions.
        if (!isAdmin &&
                !account.getUser().getId()
                        .equals(loggedInUser.getId())) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        return ResponseEntity.ok(
                transactionService
                        .getTransactionHistory(accountId)
        );
    }


    // ==========================================
    // TRANSFER MONEY
    // USER + ADMIN
    // ==========================================

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transferMoney(
            @Valid @RequestBody TransactionRequest request,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);

        String email = jwtService.extractUsername(token);

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Account sender = accountRepository.findById(
                request.getAccountId()
        ).orElseThrow(() ->
                new RuntimeException(
                        "Sender account not found"));

        // Check ADMIN authority
        boolean isAdmin = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(authority ->
                        "ADMIN".equals(authority.getAuthority()));

        // USER can transfer only from own account.
        // ADMIN can transfer from any account.
        if (!isAdmin &&
                !sender.getUser().getId()
                        .equals(loggedInUser.getId())) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        return ResponseEntity.ok(
                transactionService.transferMoney(request)
        );
    }
}
