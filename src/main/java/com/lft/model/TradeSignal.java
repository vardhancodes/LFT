package com.lft.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TradeSignal {
    private String symbol;          // The stock we are trading
    private Action action;          // BUY, SELL, or HOLD
    private double targetPrice;     // Take-profit level (e.g., current price + 5%)
    private double stopLoss;        // Maximum acceptable loss (e.g., current price - 2%)
    private double confidence;      // Probability score from your Python ML model (0.0 to 1.0)
    private String reason;          // Audit log explaining WHY this decision was made
}