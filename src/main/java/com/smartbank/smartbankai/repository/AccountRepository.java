package com.smartbank.smartbankai.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartbank.smartbankai.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	Optional<Account> findByUserId(Long userId);

}