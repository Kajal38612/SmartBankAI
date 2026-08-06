package com.smartbank.smartbankai.dto;

import com.smartbank.smartbankai.entity.TransactionType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TransactionRequest {
	@NotNull(message = "Account Id is required")
    private Long accountId;
	
	@NotNull(message = "Amount is required")
	@Positive(message = "Amount must be greater than zero")
    private Double amount;

	@NotNull(message = "Transaction Type is required")
    private TransactionType transactionType;

    private String remarks;
    
    
    private Long receiverAccountId;
    
    

    public Long getReceiverAccountId() {
        return receiverAccountId;
    }

    public void setReceiverAccountId(Long receiverAccountId) {
        this.receiverAccountId = receiverAccountId;
    }
    

    public TransactionRequest() {
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}