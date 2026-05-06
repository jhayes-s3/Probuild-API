package com.probuild;

import com.probuild.model.ServiceJob;
import com.probuild.model.Tool;
import com.probuild.repository.ServiceJobRepository;
import com.probuild.repository.ToolRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ServiceJobRepositoryTest {

    @Autowired
    private ServiceJobRepository serviceJobRepository;

    @Autowired
    private ToolRepository toolRepository;

    @Test
    void findByToolIdReturnsServiceJobsForTool() {
        Tool tool = new Tool();
        tool.setName("Mixer");
        tool.setAvailable(true);
        tool = toolRepository.save(tool);

        ServiceJob job = new ServiceJob();
        job.setDescription("Repair mixer");
        job.setServiceDate(LocalDate.now());
        job.setServiceLog("Created by test");
        job.setTool(tool);

        serviceJobRepository.save(job);

        List<ServiceJob> results = StreamSupport
                .stream(serviceJobRepository.findByToolId(tool.getId()).spliterator(), false)
                .toList();

        assertEquals(1, results.size());
        assertEquals("Repair mixer", results.get(0).getDescription());
    }
}