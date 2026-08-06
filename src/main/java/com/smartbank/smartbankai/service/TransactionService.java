package com.smartbank.smartbankai.service;

import java.util.List;

import com.smartbank.smartbankai.dto.TransactionRequest;
import com.smartbank.smartbankai.entity.Transaction;

public interface TransactionService {

    Transaction performTransaction(TransactionRequest request);
    Transaction transferMoney(TransactionRequest request);

    List<Transaction> getTransactionHistory(Long accountId);
}