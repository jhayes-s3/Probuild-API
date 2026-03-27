package com.probuild.repository;

import com.probuild.model.Booking;
import org.springframework.data.repository.CrudRepository;

public interface BookingRepository extends CrudRepository<Booking, Integer> {
    Iterable<Booking> findByCustomerId(Integer customerId);
    Iterable<Booking> findByToolId(Integer toolId);
}