package com.smartbank.smartbankai.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartbank.smartbankai.dto.AdminLoginRequest;
import com.smartbank.smartbankai.dto.AdminLoginResponse;
import com.smartbank.smartbankai.dto.AdminResponse;
import com.smartbank.smartbankai.dto.UserDTO;
import com.smartbank.smartbankai.entity.Account;
import com.smartbank.smartbankai.entity.Admin;
import com.smartbank.smartbankai.entity.Transaction;
import com.smartbank.smartbankai.entity.User;
import com.smartbank.smartbankai.exception.AccountNotFoundException;
import com.smartbank.smartbankai.exception.InvalidCredentialsException;
import com.smartbank.smartbankai.repository.AccountRepository;
import com.smartbank.smartbankai.repository.AdminRepository;
import com.smartbank.smartbankai.repository.TransactionRepository;
import com.smartbank.smartbankai.repository.UserRepository;
import com.smartbank.smartbankai.security.JwtService;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;
    
    @Override
    public AdminLoginResponse login(AdminLoginRequest request) {

        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid Email"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                admin.getPassword())) {

            throw new InvalidCredentialsException("Invalid Password");
        }

        UserDetails userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername(admin.getEmail())
                        .password(admin.getPassword())
                        .authorities("ADMIN")
                        .build();

        String token = jwtService.generateToken(userDetails);

       return new AdminLoginResponse(
            token,
            "Admin Login Successful",
            "ADMIN");
        
    }
    @Override
    public AdminResponse getProfile() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Admin not found"));

        return new AdminResponse(

                admin.getId(),
                admin.getFullName(),
                admin.getEmail(),
                admin.getStatus()
        );
    }
    
    @Override
    public List<UserDTO> getAllUsers() {

    List<User> users = userRepository.findAll();

    return users.stream()
            .map(user -> {

                UserDTO dto = new UserDTO();

                dto.setId(user.getId());
                dto.setFullName(user.getFullName());
                dto.setEmail(user.getEmail());
                dto.setMobile(user.getMobile());

                return dto;

            })
            .collect(Collectors.toList());
}
    
    @Override
    public List<Account> getAllAccounts() {

        return accountRepository.findAll();
    }
    @Override
    public List<Transaction> getAllTransactions() {

        return transactionRepository.findAll();
    }
    @Override
    public Account freezeAccount(Long accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found"));

        account.setStatus("FREEZE");

        return accountRepository.save(account);
    }
    @Override
    public Account activateAccount(Long accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found"));

        account.setStatus("ACTIVE");

        return accountRepository.save(account);
    }
    @Override
    public Account closeAccount(Long accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found"));

        account.setStatus("CLOSED");

        return accountRepository.save(account);
    }
}
    
