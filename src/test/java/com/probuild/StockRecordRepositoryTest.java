package com.probuild;

import com.probuild.model.StockRecord;
import com.probuild.model.Tool;
import com.probuild.repository.StockRecordRepository;
import com.probuild.repository.ToolRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class StockRecordRepositoryTest {

    @Autowired
    private StockRecordRepository stockRecordRepository;

    @Autowired
    private ToolRepository toolRepository;

    @Test
    void findByToolIdReturnsStockRecordsForTool() {
        Tool tool = new Tool();
        tool.setName("Drill");
        tool.setAvailable(true);
        tool = toolRepository.save(tool);

        StockRecord record = new StockRecord();
        record.setTool(tool);
        record.setQuantity(10);
        record.setBinLocation("A1");

        stockRecordRepository.save(record);

        List<StockRecord> results = StreamSupport
                .stream(stockRecordRepository.findByToolId(tool.getId()).spliterator(), false)
                .toList();

        assertEquals(1, results.size());
        assertEquals(10, results.get(0).getQuantity());
        assertEquals("A1", results.get(0).getBinLocation());
    }
}