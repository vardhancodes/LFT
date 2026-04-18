package com.lft.controller;

import com.lft.client.MlClient;
import com.lft.model.Candle;
import com.lft.model.TradeSignal;
import com.lft.service.ExecutionService;
import com.lft.service.FeatureService;
import com.lft.service.MarketDataService;
import com.lft.service.StrategyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/trade")
@RequiredArgsConstructor
public class TradeController {

    // The RequiredArgsConstructor from Lombok automatically injects these services.
    private final MarketDataService marketDataService;
    private final FeatureService featureService;
    private final MlClient mlClient;
    private final StrategyService strategyService;
    private final ExecutionService executionService;

    /**
     * Trigger a trading evaluation for a specific stock symbol.
     * Example URL: POST http://localhost:8080/api/v1/trade/run/AAPL
     */
    @PostMapping("/run/{symbol}")
    public ResponseEntity<TradeSignal> runTradingCycle(@PathVariable String symbol) {
        log.info("--- Starting trading cycle for {} ---", symbol);

        // 1. Fetch raw market data (last 50 periods)
        List<Candle> candles = marketDataService.fetchHistoricalData(symbol, 50);
        
        // 2. Generate mathematical features (SMA, RSI, etc.)
        Map<String, Double> features = featureService.generateFeatures(candles);
        
        // 3. Send features to Python ML Model and get prediction
        MlClient.MlPrediction prediction = mlClient.getPrediction(features);
        
        // 4. Run through quantitative risk strategy
        TradeSignal signal = strategyService.evaluateStrategy(symbol, features, prediction);
        
        // 5. Execute the trade (or paper trade it in our case)
        executionService.executeTrade(signal);
        
        log.info("--- Completed trading cycle for {} ---", symbol);
        
        // 6. Return the final decision back to the user/dashboard as JSON
        return ResponseEntity.ok(signal);
    }
}