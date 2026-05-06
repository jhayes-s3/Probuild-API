package com.probuild.repository;

import com.probuild.model.TradeCard;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TradeCardRepository extends CrudRepository<TradeCard, Integer> {
    Optional<TradeCard> findByCardNumber(String cardNumber);
    List<TradeCard> findByCustomerId(Integer customerId);
}
