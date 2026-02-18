package com.project.gold.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuyResponse {
    
    private String status;
    
    private Double gramsAllocated;
    
    private Double priceUsed;
    
    private String transactionId;
    
    private String paymentId;
}
