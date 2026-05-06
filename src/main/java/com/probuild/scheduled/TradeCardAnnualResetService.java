package com.probuild.scheduled;

import com.probuild.model.TradeCard;
import com.probuild.repository.TradeCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Trade card points reset annually per the domain rules. Runs at 00:00 on
 * 1st January each year. resetAll() is also exposed via the controller for
 * manual triggering during demos.
 */
@Service
public class TradeCardAnnualResetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeCardAnnualResetService.class);

    @Autowired
    private TradeCardRepository tradeCardRepository;

    @Scheduled(cron = "0 0 0 1 1 *")
    public void scheduledAnnualReset() {
        LOGGER.info("Annual trade-card points reset triggered (scheduled)");
        int count = resetAll();
        LOGGER.info("Reset points on {} trade card(s)", count);
    }

    /** Reset all trade card points to 0. Returns count of cards reset. */
    public int resetAll() {
        int count = 0;
        for (TradeCard card : tradeCardRepository.findAll()) {
            card.setPointsBalance(0);
            tradeCardRepository.save(card);
            count++;
        }
        return count;
    }
}
