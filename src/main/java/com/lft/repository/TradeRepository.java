package com.lft.repository;

import com.lft.model.TradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends JpaRepository<TradeEntity, Long> {
    // Spring Data JPA automatically gives us save(), findAll(), deleteById(), etc.
    // We don't have to write a single line of SQL!
}