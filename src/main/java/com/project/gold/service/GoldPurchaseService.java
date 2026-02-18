package com.project.gold.service;

import com.project.gold.dto.BuyRequest;
import com.project.gold.dto.BuyResponse;
import com.project.gold.dto.PaymentResponse;
import com.project.gold.exception.PaymentFailedException;
import com.project.gold.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoldPurchaseService {
    
    private final GoldPriceService goldPriceService;
    private final PaymentService paymentService;
    private final TransactionService transactionService;
    private final HoldingsService holdingsService;
    
    @Transactional
    public BuyResponse buyGold(BuyRequest buyRequest) {
        log.info("Processing gold purchase request. UserId: {}, Amount: ₹{}", 
                buyRequest.getUserId(), buyRequest.getAmount());
        
        try {
            log.info("Step 1: Fetching current gold price");
            Double currentGoldPrice = goldPriceService.getCurrentGoldPrice();
            
            if (currentGoldPrice == null || currentGoldPrice <= 0) {
                log.error("Invalid gold price received: {}", currentGoldPrice);
                throw new RuntimeException("Invalid gold price received");
            }
            
            Double grams = buyRequest.getAmount() / currentGoldPrice;
            log.info("Step 2: Calculated grams: {} for amount: ₹{} at price: ₹{}", 
                    grams, buyRequest.getAmount(), currentGoldPrice);
            
            log.info("Step 3: Initiating payment");
            PaymentResponse paymentResponse = paymentService.processPayment(
                    buyRequest.getUserId(), 
                    buyRequest.getAmount()
            );
            
            if (paymentResponse == null) {
                log.error("Payment response is null");
                throw new PaymentFailedException("Payment response is null");
            }
            
            if (!"SUCCESS".equalsIgnoreCase(paymentResponse.getStatus())) {
                log.error("Payment failed. Status: {}, Message: {}", 
                        paymentResponse.getStatus(), paymentResponse.getMessage());
                
                transactionService.saveTransaction(
                        buyRequest.getUserId(),
                        buyRequest.getAmount(),
                        currentGoldPrice,
                        grams,
                        paymentResponse.getPaymentId(),
                        "FAILED"
                );
                
                throw new PaymentFailedException("Payment failed: " + paymentResponse.getMessage());
            }
            
            log.info("Step 4: Payment successful. PaymentId: {}", paymentResponse.getPaymentId());
            
            log.info("Step 5: Saving transaction");
            Transaction transaction = transactionService.saveTransaction(
                    buyRequest.getUserId(),
                    buyRequest.getAmount(),
                    currentGoldPrice,
                    grams,
                    paymentResponse.getPaymentId(),
                    "SUCCESS"
            );
            
            log.info("Step 6: Updating holdings");
            holdingsService.updateHoldings(
                    buyRequest.getUserId(),
                    grams,
                    currentGoldPrice,
                    buyRequest.getAmount()
            );
            
            log.info("Gold purchase completed successfully. TransactionId: {}", transaction.getId());
            
            return BuyResponse.builder()
                    .status("SUCCESS")
                    .gramsAllocated(grams)
                    .priceUsed(currentGoldPrice)
                    .transactionId(transaction.getId())
                    .paymentId(paymentResponse.getPaymentId())
                    .build();
                    
        } catch (PaymentFailedException e) {
            log.error("Payment failed during gold purchase", e);
            throw e;
        } catch (Exception e) {
            log.error("Error processing gold purchase", e);
            throw new RuntimeException("Failed to process gold purchase: " + e.getMessage(), e);
        }
    }
}
