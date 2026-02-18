package com.project.gold.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "external.apis")
public class ExternalApiConfig {
    
    private GoldPrice goldPrice = new GoldPrice();
    private PaymentGateway paymentGateway = new PaymentGateway();
    
    @Data
    public static class GoldPrice {
        private String url;
        private Integer timeout;
        private Boolean cacheEnabled;
        private Integer cacheDurationSeconds;
        private String apiKey;
    }
    
    @Data
    public static class PaymentGateway {
        private String url;
        private Integer timeout;
        private String apiKey;
        private String apiSecret;
    }
}
