package com.lft.service;

import com.lft.client.MlClient;
import com.lft.model.Action;
import com.lft.model.TradeSignal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class StrategyService {

    /**
     * Evaluates the ML prediction against traditional technical indicators to make a final decision.
     */
    public TradeSignal evaluateStrategy(String symbol, Map<String, Double> features, MlClient.MlPrediction mlResult) {
        
        // Extract the features we need for our risk rules
        double currentPrice = features.getOrDefault("current_price", 0.0);
        double rsi = features.getOrDefault("rsi_14", 50.0);
        
        Action finalAction = Action.HOLD;
        String reason = "Market conditions neutral. No action taken.";

        // --- RISK RULE 1: THE BUY LOGIC ---
        // We only buy if the ML model is confident AND the stock is not already overbought (RSI < 70)
        if ("BUY".equalsIgnoreCase(mlResult.prediction()) && mlResult.confidence() > 0.70) {
            if (rsi < 70) {
                finalAction = Action.BUY;
                reason = String.format("ML Confident (%.2f) and RSI (%.2f) allows entry.", mlResult.confidence(), rsi);
            } else {
                reason = String.format("ML Buy rejected. RSI (%.2f) indicates stock is overbought.", rsi);
                log.warn("[{}] Risk Management: Blocked ML BUY due to high RSI", symbol);
            }
        } 
        
        // --- RISK RULE 2: THE SELL LOGIC ---
        // We sell if the ML model says SELL, OR if the stock is extremely overbought (RSI > 80)
        else if ("SELL".equalsIgnoreCase(mlResult.prediction()) || rsi > 80) {
            finalAction = Action.SELL;
            reason = "ML signaled SELL, or RSI hit extreme overbought levels.";
        }

        // --- RISK RULE 3: CALCULATE TARGETS ---
        // If we buy, where do we take profit? Where do we cut losses?
        double targetPrice = 0.0;
        double stopLoss = 0.0;
        
        if (finalAction == Action.BUY) {
            targetPrice = currentPrice * 1.05; // Take profit at +5%
            stopLoss = currentPrice * 0.98;    // Cut losses at -2%
        } else if (finalAction == Action.SELL) {
            targetPrice = currentPrice * 0.95; // For short selling: target -5%
            stopLoss = currentPrice * 1.02;    // Stop loss at +2%
        }

        // Build and return the final blueprint
        return TradeSignal.builder()
                .symbol(symbol)
                .action(finalAction)
                .targetPrice(targetPrice)
                .stopLoss(stopLoss)
                .confidence(mlResult.confidence())
                .reason(reason)
                .build();
    }
}