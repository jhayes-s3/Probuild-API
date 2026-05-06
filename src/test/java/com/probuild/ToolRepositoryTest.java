package com.probuild;

import com.probuild.model.Tool;
import com.probuild.repository.ToolRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ToolRepositoryTest {

    @Autowired
    private ToolRepository toolRepository;

    @Test
    void findByAvailableTrueReturnsOnlyAvailableTools() {
        Tool availableTool = new Tool();
        availableTool.setName("Drill");
        availableTool.setCategory("Power Tools");
        availableTool.setAvailable(true);

        Tool unavailableTool = new Tool();
        unavailableTool.setName("Saw");
        unavailableTool.setCategory("Power Tools");
        unavailableTool.setAvailable(false);

        toolRepository.save(availableTool);
        toolRepository.save(unavailableTool);

        List<Tool> results = StreamSupport
                .stream(toolRepository.findByAvailableTrue().spliterator(), false)
                .toList();

        assertEquals(1, results.size());
        assertEquals("Drill", results.get(0).getName());
    }

    @Test
    void findToolByIdReturnsMatchingTool() {
        Tool tool = new Tool();
        tool.setName("Mixer");
        tool.setCategory("Concrete");
        tool.setAvailable(true);

        Tool saved = toolRepository.save(tool);

        Tool result = toolRepository.findToolById(saved.getId());

        assertEquals("Mixer", result.getName());
        assertEquals("Concrete", result.getCategory());
    }
}