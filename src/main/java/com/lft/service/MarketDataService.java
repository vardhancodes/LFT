package com.lft.service;

import com.lft.model.Candle;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class MarketDataService {

    /**
     * Fetches historical OHLCV data for a given symbol.
     * @param symbol The stock ticker (e.g., "RELIANCE")
     * @param periods The number of past candles to retrieve (e.g., 50 days)
     * @return A list of Candle objects ordered from oldest to newest
     */
    public List<Candle> fetchHistoricalData(String symbol, int periods) {
        List<Candle> candles = new ArrayList<>();
        Instant now = Instant.now();
        
        // Base price to simulate a realistic stock
        double currentPrice = 150.0;

        // We loop backward to create older candles first, up to the current day
        for (int i = periods; i >= 0; i--) {
            
            // Introduce a random price swing between -2.5 and +2.5
            double fluctuation = (Math.random() * 5) - 2.5; 
            currentPrice += fluctuation;

            Candle dailyCandle = Candle.builder()
                    .symbol(symbol)
                    .timestamp(now.minus(i, ChronoUnit.DAYS))
                    .open(currentPrice - 1.0)
                    .high(currentPrice + 2.0)
                    .low(currentPrice - 2.0)
                    .close(currentPrice)
                    .volume(10000 + (long)(Math.random() * 5000))
                    .build();
            
            candles.add(dailyCandle);
        }
        
        System.out.println("Fetched " + candles.size() + " candles for " + symbol);
        return candles;
    }
}