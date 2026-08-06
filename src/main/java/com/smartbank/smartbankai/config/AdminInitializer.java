package com.smartbank.smartbankai.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.smartbank.smartbankai.entity.Admin;
import com.smartbank.smartbankai.repository.AdminRepository;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(AdminRepository adminRepository,
                            PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        if (adminRepository.findByEmail("admin@smartbank.com").isEmpty()) {

            Admin admin = new Admin();

            admin.setFullName("System Admin");
            admin.setEmail("admin@smartbank.com");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setStatus("ACTIVE");

            adminRepository.save(admin);

            System.out.println("Default Admin Created Successfully.");
        }
    }
}


//जब पहली बार Application Run होगी:
//
//यदि admins table खाली है तो automatically यह Admin create हो जाएगा।
//
//Field	Value
//Full Name	System Admin
//Email	admin@smartbank.com
//Password	Admin@123
//Status	ACTIVE