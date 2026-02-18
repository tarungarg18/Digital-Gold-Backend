package com.project.gold.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    
    @NotBlank(message = "UserId is required")
    private String userId;
    
    @NotNull(message = "Amount is required")
    private Double amount;
    
    @NotBlank(message = "Currency is required")
    private String currency;
}
