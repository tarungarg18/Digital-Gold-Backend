package com.project.gold.service;

import com.project.gold.model.Holdings;
import com.project.gold.repository.HoldingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HoldingsService {
    
    private final HoldingsRepository holdingsRepository;
    
    public void updateHoldings(String userId, Double grams, Double goldPrice, Double amount) {
        log.info("Updating holdings for userId: {}, grams: {}, price: ₹{}", 
                userId, grams, goldPrice);
        
        Optional<Holdings> existingHoldingsOpt = holdingsRepository.findByUserId(userId);
        
        Holdings holdings;
        if (existingHoldingsOpt.isPresent()) {
            holdings = existingHoldingsOpt.get();
            
            Double existingGrams = holdings.getTotalGrams() != null ? holdings.getTotalGrams() : 0.0;
            Double existingInvested = holdings.getTotalInvestedAmount() != null ? holdings.getTotalInvestedAmount() : 0.0;
            
            Double totalGrams = existingGrams + grams;
            Double totalInvested = existingInvested + amount;
            
            Double newAvgPrice = totalGrams > 0 ? totalInvested / totalGrams : 0.0;
            
            holdings.setTotalGrams(totalGrams);
            holdings.setTotalInvestedAmount(totalInvested);
            holdings.setAvgBuyPrice(newAvgPrice);
            
            log.info("Updated existing holdings. New totalGrams: {}, new avgPrice: ₹{}", 
                    totalGrams, newAvgPrice);
        } else {
            holdings = Holdings.builder()
                    .userId(userId)
                    .totalGrams(grams)
                    .avgBuyPrice(goldPrice)
                    .totalInvestedAmount(amount)
                    .build();
            
            log.info("Created new holdings. TotalGrams: {}, avgPrice: ₹{}", 
                    grams, goldPrice);
        }
        
        holdingsRepository.save(holdings);
        log.info("Holdings updated successfully for userId: {}", userId);
    }
    
    public Holdings getHoldingsByUserId(String userId) {
        log.info("Fetching holdings for userId: {}", userId);
        return holdingsRepository.findByUserId(userId)
                .orElse(Holdings.builder()
                        .userId(userId)
                        .totalGrams(0.0)
                        .avgBuyPrice(0.0)
                        .totalInvestedAmount(0.0)
                        .build());
    }
}
