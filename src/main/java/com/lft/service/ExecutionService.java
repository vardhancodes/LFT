package com.lft.service;

import com.lft.model.Action;
import com.lft.model.TradeSignal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExecutionService {

    /**
     * Routes the final signal to the broker for execution.
     */
    public void executeTrade(TradeSignal signal) {
        
        // If the strategy decided to HOLD, we do absolutely nothing.
        if (signal.getAction() == Action.HOLD) {
            log.info("[{}] HOLD signal received. Reason: {}", signal.getSymbol(), signal.getReason());
            return;
        }

        // In a real system, you would initialize your Broker API Client here 
        // (e.g., calling Zerodha Kite Connect or Upstox API).
        
        log.info("=================================================");
        log.info("🚀 EXECUTING TRADE: {} {}", signal.getAction(), signal.getSymbol());
        log.info("🎯 Target Price: ${}", String.format("%.2f", signal.getTargetPrice()));
        log.info("🛡️ Stop Loss: ${}", String.format("%.2f", signal.getStopLoss()));
        log.info("🧠 ML Confidence: {}%", String.format("%.2f", signal.getConfidence() * 100));
        log.info("📝 Audit Reason: {}", signal.getReason());
        log.info("=================================================");
        
        // TODO: Http call to broker: brokerClient.placeOrder(signal);
    }
}