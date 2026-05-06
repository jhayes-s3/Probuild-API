package com.probuild;

import com.probuild.model.Customer;
import com.probuild.model.TradeCard;
import com.probuild.repository.CustomerRepository;
import com.probuild.repository.TradeCardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class TradeCardRepositoryTest {

    @Autowired
    private TradeCardRepository tradeCardRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void findByCardNumberReturnsMatchingTradeCard() {
        Customer customer = new Customer();
        customer.setName("James");
        customer = customerRepository.save(customer);

        TradeCard card = new TradeCard();
        card.setCardNumber("CARD-123");
        card.setPointsBalance(50);
        card.setIssueDate(LocalDate.now());
        card.setCustomer(customer);

        tradeCardRepository.save(card);

        Optional<TradeCard> result = tradeCardRepository.findByCardNumber("CARD-123");

        assertTrue(result.isPresent());
        assertEquals(50, result.get().getPointsBalance());
    }

    @Test
    void findByCustomerIdReturnsCustomerCards() {
        Customer customer = new Customer();
        customer.setName("James");
        customer = customerRepository.save(customer);

        TradeCard card = new TradeCard();
        card.setCardNumber("CARD-456");
        card.setPointsBalance(20);
        card.setIssueDate(LocalDate.now());
        card.setCustomer(customer);

        tradeCardRepository.save(card);

        List<TradeCard> results = tradeCardRepository.findByCustomerId(customer.getId());

        assertEquals(1, results.size());
        assertEquals("CARD-456", results.get(0).getCardNumber());
    }
}