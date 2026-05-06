package com.probuild;

import com.probuild.model.Booking;
import com.probuild.model.Customer;
import com.probuild.model.Tool;
import com.probuild.repository.BookingRepository;
import com.probuild.repository.CustomerRepository;
import com.probuild.repository.ToolRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ToolRepository toolRepository;

    @Test
    void findByCustomerIdReturnsCustomerBookings() {
        Customer customer = new Customer();
        customer.setName("James");
        customer = customerRepository.save(customer);

        Tool tool = new Tool();
        tool.setName("Drill");
        tool.setAvailable(true);
        tool = toolRepository.save(tool);

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setTool(tool);
        booking.setStartDate(LocalDate.now());
        booking.setEndDate(LocalDate.now().plusDays(2));
        booking.setDepositAmount(50.00);
        booking.setPaymentType("card");

        bookingRepository.save(booking);

        List<Booking> results = StreamSupport
                .stream(bookingRepository.findByCustomerId(customer.getId()).spliterator(), false)
                .toList();

        assertEquals(1, results.size());
        assertEquals("Drill", results.get(0).getTool().getName());
    }

    @Test
    void findByToolIdReturnsToolBookings() {
        Customer customer = new Customer();
        customer.setName("James");
        customer = customerRepository.save(customer);

        Tool tool = new Tool();
        tool.setName("Saw");
        tool.setAvailable(true);
        tool = toolRepository.save(tool);

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setTool(tool);
        booking.setStartDate(LocalDate.now());
        booking.setEndDate(LocalDate.now().plusDays(1));

        bookingRepository.save(booking);

        List<Booking> results = StreamSupport
                .stream(bookingRepository.findByToolId(tool.getId()).spliterator(), false)
                .toList();

        assertEquals(1, results.size());
        assertEquals("Saw", results.get(0).getTool().getName());
    }
}