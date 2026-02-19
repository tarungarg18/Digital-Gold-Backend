package com.project.gold.controller;

import com.project.gold.dto.BuyRequest;
import com.project.gold.dto.BuyResponse;
import com.project.gold.dto.HoldingsResponse;
import com.project.gold.model.Transaction;
import com.project.gold.service.GoldPurchaseService;
import com.project.gold.service.GoldPriceService;
import com.project.gold.service.PortfolioService;
import com.project.gold.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gold")
@RequiredArgsConstructor
@Slf4j
public class GoldController {

    private final GoldPurchaseService goldPurchaseService;
    private final GoldPriceService goldPriceService;
    private final PortfolioService portfolioService;
    private final TransactionService transactionService;

    @PostMapping("/buy")
    public ResponseEntity<BuyResponse> buyGold(@Valid @RequestBody BuyRequest buyRequest) {
        log.info("Received buy gold request. UserId: {}, Amount: ₹{}",
                buyRequest.getUserId(), buyRequest.getAmount());

        BuyResponse response = goldPurchaseService.buyGold(buyRequest);

        log.info("Buy gold request processed successfully. TransactionId: {}",
                response.getTransactionId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/holdings/{userId}")
    public ResponseEntity<HoldingsResponse> getHoldings(@PathVariable String userId) {
        log.info("Received get holdings request for userId: {}", userId);

        HoldingsResponse holdings = portfolioService.getHoldings(userId);

        log.info("Holdings retrieved successfully for userId: {}", userId);

        return ResponseEntity.ok(holdings);
    }

    @GetMapping("/transactions/{userId}")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable String userId) {
        log.info("Received get transactions request for userId: {}", userId);

        List<Transaction> transactions = transactionService.getTransactionsByUserId(userId);

        log.info("Retrieved {} transactions for userId: {}", transactions.size(), userId);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/price")
    public ResponseEntity<Double> getCurrentPrice() {
        log.info("Received get current price request");

        Double price = goldPriceService.getCurrentGoldPrice();

        log.info("Current gold price: ₹{} per gram", price);

        return ResponseEntity.ok(price);
    }
}
