package com.probuild.repository;

import com.probuild.model.ServiceJob;
import org.springframework.data.repository.CrudRepository;

public interface ServiceJobRepository extends CrudRepository<ServiceJob, Integer> {
    Iterable<ServiceJob> findByToolId(Integer toolId);
}