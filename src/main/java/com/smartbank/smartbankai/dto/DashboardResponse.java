package com.smartbank.smartbankai.dto;

import java.util.List;

import com.smartbank.smartbankai.entity.Transaction;

public class DashboardResponse {

    private String fullName;
    private String accountNumber;
    private String accountType;
    private Double balance;
    private String status;
    private List<Transaction> recentTransactions;

    public DashboardResponse() {
    }

    public DashboardResponse(String fullName,
                             String accountNumber,
                             String accountType,
                             Double balance,
                             String status,
                             List<Transaction> recentTransactions) {
        this.fullName = fullName;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        this.status=status;
        this.recentTransactions = recentTransactions;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public List<Transaction> getRecentTransactions() {
        return recentTransactions;
    }

    public void setRecentTransactions(List<Transaction> recentTransactions) {
        this.recentTransactions = recentTransactions;
    }
}