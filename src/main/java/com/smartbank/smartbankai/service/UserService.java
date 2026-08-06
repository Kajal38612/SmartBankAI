package com.smartbank.smartbankai.service;

import java.util.List;
import java.util.Optional;

import com.smartbank.smartbankai.dto.LoginRequest;
import com.smartbank.smartbankai.dto.LoginResponse;
import com.smartbank.smartbankai.dto.UserDTO;
import com.smartbank.smartbankai.entity.User;

public interface UserService {

    User registerUser(User user);

	
	List<UserDTO> getAllUsers();

	Optional<User> getUserById(Long id);
	

	Optional<User> updateUser(Long id, User user);
	

	Optional<User> deleteUser(Long id);
	LoginResponse login(LoginRequest request);

}