package com.probuild.repository;

import com.probuild.model.Invoice;
import org.springframework.data.repository.CrudRepository;

public interface InvoiceRepository extends CrudRepository<Invoice, Integer> {
    Iterable<Invoice> findByCustomerId(Integer customerId);
}