package com.smartbank.smartbankai.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartbank.smartbankai.dto.LoginRequest;
import com.smartbank.smartbankai.dto.LoginResponse;
import com.smartbank.smartbankai.dto.UserDTO;
import com.smartbank.smartbankai.entity.User;
import com.smartbank.smartbankai.exception.EmailAlreadyExistsException;
import com.smartbank.smartbankai.exception.InvalidCredentialsException;
import com.smartbank.smartbankai.repository.UserRepository;
import com.smartbank.smartbankai.security.JwtService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Override
    public User registerUser(User user) {

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
    @Override
    public List<UserDTO> getAllUsers() {

        List<User> users = userRepository.findAll();

        return users.stream().map(user -> {

            UserDTO dto = new UserDTO();

            dto.setId(user.getId());
            dto.setFullName(user.getFullName());
            dto.setEmail(user.getEmail());
            dto.setMobile(user.getMobile());

            return dto;

        }).collect(Collectors.toList());
    }
    
    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    @Override
    public Optional<User> updateUser(Long id, User user) {
    	

        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()) {

            User updatedUser = existingUser.get();

            updatedUser.setFullName(user.getFullName());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setMobile(user.getMobile());

            return Optional.of(userRepository.save(updatedUser));
        }

        return Optional.empty();
    }
    
    @Override
    public Optional<User> deleteUser(Long id) {

        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()) {

            userRepository.deleteById(id);

            return existingUser;
        }

        return Optional.empty();
    }
    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid Email"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid Password");
        }

        UserDetails userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password(user.getPassword())
                        .authorities("USER")
                        .build();

        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(
                token,
                "Login Successful"
        );
    }

}