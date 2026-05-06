package com.probuild;

import com.probuild.model.Customer;
import com.probuild.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void findByNameReturnsMatchingCustomer() {
        Customer customer = new Customer();
        customer.setName("James");
        customer.setEmail("james@example.com");
        customer.setMembershipLevel("Gold");

        customerRepository.save(customer);

        Customer result = customerRepository.findByName("James");

        assertEquals("James", result.getName());
        assertEquals("james@example.com", result.getEmail());
        assertEquals("Gold", result.getMembershipLevel());
    }
}