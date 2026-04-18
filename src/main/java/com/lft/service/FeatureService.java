package com.lft.service;

import com.lft.model.Candle;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FeatureService {

    /**
     * Takes a list of raw candles and converts them into a Map of features 
     * that our Python Machine Learning model can understand.
     */
    public Map<String, Double> generateFeatures(List<Candle> candles) {
        Map<String, Double> features = new HashMap<>();
        
        // We need the most recent candle for the current price
        double currentPrice = candles.get(candles.size() - 1).getClose();
        
        // Calculate our indicators (using standard 20-day SMA and 14-day RSI)
        double sma20 = calculateSMA(candles, 20);
        double rsi14 = calculateRSI(candles, 14);

        // Populate the feature map
        features.put("current_price", currentPrice);
        features.put("sma_20", sma20);
        features.put("rsi_14", rsi14);
        
        // Ratio: Is the current price above or below its average?
        // (If > 1.0, it's above average. If < 1.0, it's below average)
        features.put("price_to_sma_ratio", sma20 > 0 ? currentPrice / sma20 : 1.0);
        
        System.out.println("Generated features: " + features);
        return features;
    }

    /**
     * Calculates the Simple Moving Average (SMA)
     */
    private double calculateSMA(List<Candle> candles, int period) {
        // If we don't have enough data, return 0 to avoid errors
        if (candles.size() < period) {
            return 0.0;
        }
        
        double sum = 0;
        // Loop through the last 'period' candles
        for (int i = candles.size() - period; i < candles.size(); i++) {
            sum += candles.get(i).getClose();
        }
        return sum / period;
    }

    /**
     * Calculates the Relative Strength Index (RSI)
     */
    private double calculateRSI(List<Candle> candles, int period) {
        // RSI requires at least period + 1 candles to calculate price differences
        if (candles.size() < period + 1) {
            return 50.0; // Default neutral RSI
        }

        double gains = 0;
        double losses = 0;

        // Loop to find the average gains and losses over the period
        for (int i = candles.size() - period; i < candles.size(); i++) {
            double currentClose = candles.get(i).getClose();
            double previousClose = candles.get(i - 1).getClose();
            double diff = currentClose - previousClose;

            if (diff >= 0) {
                gains += diff;
            } else {
                losses -= diff; // Subtracting a negative makes it positive
            }
        }

        double avgGain = gains / period;
        double avgLoss = losses / period;
        
        // If there were no losses, the stock only went up. RSI is 100 (Max)
        if (avgLoss == 0) {
            return 100.0;
        }
        
        // RSI Formula
        double rs = avgGain / avgLoss;
        return 100.0 - (100.0 / (1.0 + rs));
    }
}