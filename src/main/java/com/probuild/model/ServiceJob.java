package com.probuild.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class ServiceJob {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String description;
    private LocalDate serviceDate;
    private String serviceLog;

    @ManyToOne
    private Tool tool;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getServiceDate() { return serviceDate; }
    public void setServiceDate(LocalDate serviceDate) { this.serviceDate = serviceDate; }

    public String getServiceLog() { return serviceLog; }
    public void setServiceLog(String serviceLog) { this.serviceLog = serviceLog; }

    public Tool getTool() { return tool; }
    public void setTool(Tool tool) { this.tool = tool; }
}