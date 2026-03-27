package com.probuild.controller;

import com.probuild.model.*;
import com.probuild.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")  // Prefix all endpoints with /api
public class ProbuildController {

    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private StockRecordRepository stockRecordRepository;

    @Autowired
    private ServiceJobRepository serviceJobRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    // ========================
    // Tool Endpoints
    // ========================

    // Add a new tool
    @PostMapping("/tools")
    public Tool addTool(@RequestParam String name,
                        @RequestParam String category) {
        Tool tool = new Tool();
        tool.setName(name);
        tool.setCategory(category);
        tool.setAvailable(true);
        return toolRepository.save(tool);
    }

    // List all tools
    @GetMapping("/tools")
    public Iterable<Tool> listTools() {
        return toolRepository.findAll();
    }

    // Get tool by ID
    @GetMapping("/tools/{id}")
    public Optional<Tool> findToolById(@PathVariable Integer id) {
        return toolRepository.findById(id);
    }

    // List only available tools
    @GetMapping("/tools/available")
    public Iterable<Tool> availableTools() {
        return toolRepository.findByAvailableTrue();
    }

    // Update tool availability
    @PutMapping("/tools/{id}/availability")
    public Tool updateAvailability(@PathVariable Integer id,
                                   @RequestParam boolean available) {
        Tool tool = toolRepository.findById(id).orElseThrow();
        tool.setAvailable(available);
        return toolRepository.save(tool);
    }

    // ========================
    // Customer Endpoints
    // ========================

    @PostMapping("/customers")
    public Customer addCustomer(@RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @GetMapping("/customers")
    public Iterable<Customer> listCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("/customers/{id}")
    public Optional<Customer> getCustomer(@PathVariable Integer id) {
        return customerRepository.findById(id);
    }

    // ========================
    // Booking Endpoints
    // ========================

    @PostMapping("/bookings")
    public Booking addBooking(@RequestBody Booking booking) {
        return bookingRepository.save(booking);
    }

    @GetMapping("/bookings")
    public Iterable<Booking> listBookings() {
        return bookingRepository.findAll();
    }

    @GetMapping("/bookings/customer/{customerId}")
    public Iterable<Booking> bookingsByCustomer(@PathVariable Integer customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    @GetMapping("/bookings/tool/{toolId}")
    public Iterable<Booking> bookingsByTool(@PathVariable Integer toolId) {
        return bookingRepository.findByToolId(toolId);
    }

    // ========================
    // StockRecord Endpoints
    // ========================

    @GetMapping("/stock")
    public Iterable<StockRecord> listStock() {
        return stockRecordRepository.findAll();
    }

    @GetMapping("/stock/tool/{toolId}")
    public Iterable<StockRecord> stockByTool(@PathVariable Integer toolId) {
        return stockRecordRepository.findByToolId(toolId);
    }

    // ========================
    // ServiceJob Endpoints
    // ========================

    @GetMapping("/servicejobs")
    public Iterable<ServiceJob> listServiceJobs() {
        return serviceJobRepository.findAll();
    }

    @GetMapping("/servicejobs/tool/{toolId}")
    public Iterable<ServiceJob> serviceJobsByTool(@PathVariable Integer toolId) {
        return serviceJobRepository.findByToolId(toolId);
    }

    // ========================
    // PurchaseOrder Endpoints
    // ========================

    @GetMapping("/purchaseorders")
    public Iterable<PurchaseOrder> listPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    @GetMapping("/purchaseorders/supplier/{supplierName}")
    public Iterable<PurchaseOrder> ordersBySupplier(@PathVariable String supplierName) {
        return purchaseOrderRepository.findBySupplierName(supplierName);
    }

    // ========================
    // Invoice Endpoints
    // ========================

    @GetMapping("/invoices")
    public Iterable<Invoice> listInvoices() {
        return invoiceRepository.findAll();
    }

    @GetMapping("/invoices/customer/{customerId}")
    public Iterable<Invoice> invoicesByCustomer(@PathVariable Integer customerId) {
        return invoiceRepository.findByCustomerId(customerId);
    }
}