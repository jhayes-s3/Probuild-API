package com.probuild;

import com.probuild.model.Customer;
import com.probuild.model.Invoice;
import com.probuild.repository.CustomerRepository;
import com.probuild.repository.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class InvoiceRepositoryTest {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void findByCustomerIdReturnsCustomerInvoices() {
        Customer customer = new Customer();
        customer.setName("James");
        customer = customerRepository.save(customer);

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("INV-123");
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setAmount(250.00);
        invoice.setCustomer(customer);

        invoiceRepository.save(invoice);

        List<Invoice> results = StreamSupport
                .stream(invoiceRepository.findByCustomerId(customer.getId()).spliterator(), false)
                .toList();

        assertEquals(1, results.size());
        assertEquals("INV-123", results.get(0).getInvoiceNumber());
    }
}