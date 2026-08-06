package com.smartbank.smartbankai.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.smartbank.smartbankai.entity.Admin;
import com.smartbank.smartbankai.entity.User;
import com.smartbank.smartbankai.repository.AdminRepository;
import com.smartbank.smartbankai.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.get().getEmail())
                    .password(user.get().getPassword())
                    .authorities("USER")
                    .build();
        }

        Optional<Admin> admin = adminRepository.findByEmail(email);

        if (admin.isPresent()) {

            return org.springframework.security.core.userdetails.User
                    .withUsername(admin.get().getEmail())
                    .password(admin.get().getPassword())
                    .authorities("ADMIN")
                    .build();
        }

        throw new UsernameNotFoundException("User/Admin not found");
    }
}