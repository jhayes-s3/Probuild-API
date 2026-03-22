package com.probuild.repository;

import com.probuild.model.Tool;
import org.springframework.data.repository.CrudRepository;

public interface ToolRepository extends CrudRepository<Tool, Integer> {
    Tool findToolById(Integer id);
    Iterable<Tool> findByAvailableTrue();
}