package com.probuild.repository;

import com.probuild.model.StockRecord;
import org.springframework.data.repository.CrudRepository;

public interface StockRecordRepository extends CrudRepository<StockRecord, Integer> {
    Iterable<StockRecord> findByToolId(Integer toolId);
}