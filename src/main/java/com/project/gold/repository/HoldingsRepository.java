package com.project.gold.repository;

import com.project.gold.model.Holdings;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HoldingsRepository extends MongoRepository<Holdings, String> {
    
    Optional<Holdings> findByUserId(String userId);
}
