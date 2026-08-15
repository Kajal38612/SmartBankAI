package com.smartbank.smartbankai.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.smartbank.smartbankai.dto.AdminLoginRequest;
import com.smartbank.smartbankai.dto.AdminLoginResponse;
import com.smartbank.smartbankai.dto.AdminResponse;
import com.smartbank.smartbankai.entity.Account;
import com.smartbank.smartbankai.entity.Transaction;
import com.smartbank.smartbankai.entity.User;
import com.smartbank.smartbankai.service.AdminService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Admin Login
    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponse> login(
            @Valid @RequestBody AdminLoginRequest request) {

        return ResponseEntity.ok(adminService.login(request));
    }

    // Admin Profile
    @GetMapping("/profile")
    public ResponseEntity<AdminResponse> getProfile() {

        return ResponseEntity.ok(adminService.getProfile());
    }

    // All Users
   @GetMapping("/users")
    public List<UserDTO> getAllUsers() {

    return adminService.getAllUsers();
    }

    // All Accounts
    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {

        return adminService.getAllAccounts();
    }

    // All Transactions
    @GetMapping("/transactions")
    public List<Transaction> getAllTransactions() {

        return adminService.getAllTransactions();
    }

    // Freeze Account
    @PutMapping("/freeze/{accountId}")
    public Account freezeAccount(@PathVariable Long accountId) {

        return adminService.freezeAccount(accountId);
    }

    // Activate Account
    @PutMapping("/activate/{accountId}")
    public Account activateAccount(@PathVariable Long accountId) {

        return adminService.activateAccount(accountId);
    }

    // Close Account
    @PutMapping("/close/{accountId}")
    public Account closeAccount(@PathVariable Long accountId) {

        return adminService.closeAccount(accountId);
    }
}
