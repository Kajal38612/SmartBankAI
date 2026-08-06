package com.smartbank.smartbankai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartbank.smartbankai.dto.DashboardResponse;
import com.smartbank.smartbankai.entity.Account;
import com.smartbank.smartbankai.entity.User;
import com.smartbank.smartbankai.repository.AccountRepository;
import com.smartbank.smartbankai.repository.UserRepository;
import com.smartbank.smartbankai.security.JwtService;
import com.smartbank.smartbankai.service.DashboardService;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);

        String email = jwtService.extractUsername(token);

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = accountRepository.findByUserId(loggedInUser.getId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        DashboardResponse response =
                dashboardService.getDashboard(account.getId());

        return ResponseEntity.ok(response);
    }
}