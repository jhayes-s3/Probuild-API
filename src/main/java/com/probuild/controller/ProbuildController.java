package com.probuild.controller;

import com.probuild.model.Tool;
import com.probuild.repository.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProbuildController {

    @Autowired
    private ToolRepository toolRepository;

    // Add a tool — maps to "Update tool availability in booking system"
    @PostMapping("/add")
    public String addTool(@RequestParam String name,
                          @RequestParam String category) {
        Tool tool = new Tool();
        tool.setName(name);
        tool.setCategory(category);
        tool.setAvailable(true);
        toolRepository.save(tool);
        return "Added tool: " + name;
    }

    // List all tools
    @GetMapping("/list")
    public Iterable<Tool> listTools() {
        return toolRepository.findAll();
    }

    // Find by ID — maps to "Query tool availability in system"
    @GetMapping("/find/{id}")
    public Tool findToolById(@PathVariable Integer id) {
        return toolRepository.findToolById(id);
    }

    // List only available tools — feeds the "Is tool available?" gateway
    @GetMapping("/available")
    public Iterable<Tool> availableTools() {
        return toolRepository.findByAvailableTrue();
    }

    // Update availability — maps to "Update tool availability in booking system"
    @PostMapping("/update/{id}")
    public String updateAvailability(@PathVariable Integer id,
                                     @RequestParam boolean available) {
        Tool tool = toolRepository.findById(id).orElseThrow();
        tool.setAvailable(available);
        toolRepository.save(tool);
        return "Tool " + id + " availability updated to: " + available;
    }
}
