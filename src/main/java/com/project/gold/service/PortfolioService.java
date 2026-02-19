package com.project.gold.service;

import com.project.gold.dto.HoldingsResponse;
import com.project.gold.model.Holdings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PortfolioService {

    private final HoldingsService holdingsService;
    private final GoldPriceService goldPriceService;

    public HoldingsResponse getHoldings(String userId) {
        log.info("Calculating portfolio for userId: {}", userId);

        Holdings holdings = holdingsService.getHoldingsByUserId(userId);

        if (holdings.getTotalGrams() == null || holdings.getTotalGrams() == 0) {
            log.info("No holdings found for userId: {}", userId);
            return HoldingsResponse.builder()
                    .userId(userId)
                    .totalGrams(0.0)
                    .currentValue(0.0)
                    .investedAmount(0.0)
                    .profitLossAmount(0.0)
                    .profitLossPercent(0.0)
                    .avgBuyPrice(0.0)
                    .build();
        }

        Double currentPrice = goldPriceService.getCurrentGoldPrice();

        if (currentPrice == null) {
            log.warn("Current gold price is null, using fallback");
            currentPrice = 6000.0;
        }

        Double currentValue = holdings.getTotalGrams() * currentPrice;

        Double investedAmount = holdings.getTotalInvestedAmount() != null ?
                holdings.getTotalInvestedAmount() : 0.0;

        Double profitLossAmount = currentValue - investedAmount;
        Double profitLossPercent = investedAmount > 0 ?
                ((profitLossAmount / investedAmount) * 100) : 0.0;

        log.info("Portfolio calculated. TotalGrams: {}, CurrentValue: ₹{}, ProfitLoss: {}%",
                holdings.getTotalGrams(), currentValue, profitLossPercent);

        return HoldingsResponse.builder()
                .userId(userId)
                .totalGrams(holdings.getTotalGrams())
                .currentValue(currentValue)
                .investedAmount(investedAmount)
                .profitLossAmount(profitLossAmount)
                .profitLossPercent(profitLossPercent)
                .avgBuyPrice(holdings.getAvgBuyPrice() != null ? holdings.getAvgBuyPrice() : 0.0)
                .build();
    }
}
