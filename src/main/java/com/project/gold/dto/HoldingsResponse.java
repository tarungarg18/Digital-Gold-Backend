package com.project.gold.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HoldingsResponse {
    
    private String userId;
    
    private Double totalGrams;
    
    private Double currentValue;
    
    private Double investedAmount;
    
    private Double profitLossAmount;
    
    private Double profitLossPercent;
    
    private Double avgBuyPrice;
}
