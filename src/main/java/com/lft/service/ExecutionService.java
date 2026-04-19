package com.lft.service;

import com.lft.model.Action;
import com.lft.model.TradeEntity;
import com.lft.model.TradeSignal;
import com.lft.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExecutionService {

    // Inject our new database repository
    private final TradeRepository tradeRepository;

    public void executeTrade(TradeSignal signal) {
        if (signal.getAction() == Action.HOLD) {
            log.info("[{}] HOLD signal received. Reason: {}", signal.getSymbol(), signal.getReason());
            return;
        }

        log.info("🚀 EXECUTING TRADE: {} {}", signal.getAction(), signal.getSymbol());
        
        // Convert the TradeSignal into a TradeEntity for the database
        TradeEntity tradeRecord = TradeEntity.builder()
                .symbol(signal.getSymbol())
                .action(signal.getAction())
                .targetPrice(signal.getTargetPrice())
                .stopLoss(signal.getStopLoss())
                .mlConfidence(signal.getConfidence())
                .reason(signal.getReason())
                .executionTime(LocalDateTime.now())
                .build();

        // Save it to PostgreSQL!
        tradeRepository.save(tradeRecord);
        log.info("💾 Trade successfully saved to database with ID: {}", tradeRecord.getId());
    }
}