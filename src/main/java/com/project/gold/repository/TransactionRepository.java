package com.project.gold.repository;

import com.project.gold.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    
    List<Transaction> findByUserIdOrderByCreatedAtDesc(String userId);
    
    List<Transaction> findByUserIdAndStatus(String userId, String status);
}
