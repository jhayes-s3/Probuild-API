package com.probuild.repository;

import com.probuild.model.PurchaseOrder;
import org.springframework.data.repository.CrudRepository;

public interface PurchaseOrderRepository extends CrudRepository<PurchaseOrder, Integer> {
    Iterable<PurchaseOrder> findBySupplierName(String supplierName);
}