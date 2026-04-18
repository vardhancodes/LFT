package com.lft.model;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;

@Data
@Builder


public class Candle {
    private String symbol;      // e.g., "AAPL" or "RELIANCE"
    private Instant timestamp;  // The exact time this candle closed
    private double open;        // Price at the start of the period
    private double high;        // Highest price during the period
    private double low;         // Lowest price during the period
    private double close;       // Price at the end of the period 
    private long volume;        // Number of shares traded

}
