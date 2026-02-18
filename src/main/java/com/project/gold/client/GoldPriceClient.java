package com.project.gold.client;

import com.project.gold.config.ExternalApiConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoldPriceClient {
    
    private final WebClient.Builder webClientBuilder;
    private final ExternalApiConfig externalApiConfig;
    
    public Double fetchGoldPrice() {
        String apiUrl = externalApiConfig.getGoldPrice().getUrl();
        String apiKey = externalApiConfig.getGoldPrice().getApiKey();
        log.info("Fetching gold price from external API: {}", apiUrl);
        
        try {
            WebClient webClient = webClientBuilder.baseUrl(apiUrl).build();
            
            Map<String, Object> response = webClient.get()
                    .uri(uriBuilder -> {
                        uriBuilder.queryParam("api_key", apiKey != null ? apiKey : "");
                        uriBuilder.queryParam("base", "INR");
                        uriBuilder.queryParam("currencies", "");
                        return uriBuilder.build();
                    })
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .timeout(Duration.ofMillis(externalApiConfig.getGoldPrice().getTimeout()))
                    .onErrorResume(WebClientResponseException.class, ex -> {
                        log.error("Gold price API failed. Status: {}, Body: {}", 
                                ex.getStatusCode(), ex.getResponseBodyAsString());
                        return Mono.empty();
                    })
                    .onErrorResume(Exception.class, ex -> {
                        log.error("Gold price API error: {}", ex.getMessage());
                        return Mono.empty();
                    })
                    .block();
            
            if (response == null || response.isEmpty()) {
                log.error("Empty response from gold price API");
                return 100000.0;
            }
            
            Double pricePerGram = parseGoldPrice(response);
            
            if (pricePerGram != null && pricePerGram > 0) {
                log.info("Gold price fetched successfully: ₹{} per gram", pricePerGram);
                return pricePerGram;
            } else {
                log.error("Could not parse gold price from response: {}", response);
                return 100000.0;
            }
            
        } catch (Exception e) {
            log.error("Failed to fetch gold price: {}", e.getMessage(), e);
            return 100000.0;
        }
    }
    
    private Double parseGoldPrice(Map<String, Object> response) {
        if (response == null || response.isEmpty()) {
            return null;
        }
        
        try {
            log.debug("Parsing gold price from response: {}", response);
            
            if (response.containsKey("rates")) {
                Object ratesObj = response.get("rates");
                if (ratesObj instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> rates = (Map<String, Object>) ratesObj;
                    
                    for (String currency : rates.keySet()) {
                        Object rateObj = rates.get(currency);
                        if (rateObj instanceof Number) {
                            Double rate = ((Number) rateObj).doubleValue();
                            
                            if (rate > 0) {
                                Double pricePerGram = 100000.0 + (rate * 100);
                                log.info("Found rate {} for {}, converted to ₹{} per gram", 
                                        rate, currency, pricePerGram);
                                return pricePerGram;
                            }
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            log.error("Error parsing gold price from response: {}", e.getMessage(), e);
        }
        
        return null;
    }
}
