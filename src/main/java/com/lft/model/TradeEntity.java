package com.lft.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "trade_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Auto-incrementing primary key

    private String symbol;
    
    @Enumerated(EnumType.STRING)
    private Action action;

    private double targetPrice;
    private double stopLoss;
    private double mlConfidence;
    private String reason;
    
    private LocalDateTime executionTime;
}