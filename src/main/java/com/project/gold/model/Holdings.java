package com.project.gold.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "holdings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Holdings {
    
    @Id
    private String id;
    
    private String userId;
    
    @Builder.Default
    private Double totalGrams = 0.0;
    
    @Builder.Default
    private Double avgBuyPrice = 0.0;
    
    @Builder.Default
    private Double totalInvestedAmount = 0.0;
}
