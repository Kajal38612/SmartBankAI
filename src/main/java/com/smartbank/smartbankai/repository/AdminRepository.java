package com.smartbank.smartbankai.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartbank.smartbankai.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByEmail(String email);

}