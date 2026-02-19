package com.project.gold.service;

import com.project.gold.client.GoldPriceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoldPriceService {

    private final GoldPriceClient goldPriceClient;

    public Double getCurrentGoldPrice() {
        log.info("Fetching current gold price");

        Double price = goldPriceClient.fetchGoldPrice();

        if (price == null) {
            log.warn("Received null price from client. Using fallback value.");
            price = 6000.0;
        }

        log.info("Current gold price: ₹{} per gram", price);
        return price;
    }
}
