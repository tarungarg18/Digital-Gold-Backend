package com.project.gold.service;

import com.project.gold.model.Transaction;
import com.project.gold.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public Transaction saveTransaction(String userId, Double amount, Double goldPrice,
                                      Double grams, String paymentId, String status) {
        log.info("Saving transaction for userId: {}, amount: ₹{}, grams: {}",
                userId, amount, grams);

        Transaction transaction = Transaction.builder()
                .userId(userId)
                .amount(amount)
                .goldPrice(goldPrice)
                .grams(grams)
                .paymentId(paymentId)
                .status(status)
                .createdAt(LocalDateTime.now())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transaction saved successfully. TransactionId: {}", savedTransaction.getId());

        return savedTransaction;
    }

    public List<Transaction> getTransactionsByUserId(String userId) {
        log.info("Fetching transactions for userId: {}", userId);
        List<Transaction> transactions = transactionRepository.findByUserIdOrderByCreatedAtDesc(userId);
        log.info("Found {} transactions for userId: {}", transactions.size(), userId);
        return transactions;
    }
}
