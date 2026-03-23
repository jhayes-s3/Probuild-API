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
        System.out.println("Add tool endpoint hit");
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
        System.out.println("List tool endpoint hit");
        return toolRepository.findAll();
    }

    // Find by ID — maps to "Query tool availability in system"
    @GetMapping("/find/{id}")
    public Tool findToolById(@PathVariable Integer id) {
        System.out.println("Find tool endpoint hit");
        return toolRepository.findToolById(id);
    }

    // List only available tools — feeds the "Is tool available?" gateway
    @GetMapping("/available")
    public Iterable<Tool> availableTools() {
        System.out.println("Available tools endpoint hit");
        return toolRepository.findByAvailableTrue();
    }

    // Update availability — maps to "Update tool availability in booking system"
    @PostMapping("/update/{id}")
    public String updateAvailability(@PathVariable Integer id,
                                     @RequestParam boolean available) {
        System.out.println("Update tool endpoint hit with id:" + id);
        Tool tool = toolRepository.findById(id).orElseThrow();
        tool.setAvailable(available);
        toolRepository.save(tool);
        return "Tool " + id + " availability updated to: " + available;
    }
}
