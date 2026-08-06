package com.smartbank.smartbankai.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full Name is required")
    private String fullName;
    
    @Email(message = "Invalid Email")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 10, max = 10, message = "Mobile must be 10 digits")
    @NotBlank(message = "Mobile is required")
    private String mobile;

    @Size(min = 6, message = "Password must be at least 6 characters")
    @NotBlank(message = "Password is required")
    private String password;
    

    // Default Constructor
    public User() {

    }

    // Parameterized Constructor
    public User(Long id, String fullName, String email, String mobile, String password) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
    }

    // Getter and Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @JsonManagedReference
    @OneToOne(mappedBy = "user")
    private Account account;
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}