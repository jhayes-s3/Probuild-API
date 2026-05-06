package com.probuild;

import com.probuild.model.PurchaseOrder;
import com.probuild.repository.PurchaseOrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class PurchaseOrderRepositoryTest {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Test
    void findBySupplierNameReturnsMatchingPurchaseOrders() {
        PurchaseOrder order = new PurchaseOrder();
        order.setSupplierName("supplier");
        order.setOrderDate(LocalDate.now());
        order.setExpectedDeliveryDate(LocalDate.now().plusDays(7));
        order.setDeliveryManifest("MAN-123");

        purchaseOrderRepository.save(order);

        List<PurchaseOrder> results = StreamSupport
                .stream(purchaseOrderRepository.findBySupplierName("supplier").spliterator(), false)
                .toList();

        assertEquals(1, results.size());
        assertEquals("MAN-123", results.get(0).getDeliveryManifest());
    }
}