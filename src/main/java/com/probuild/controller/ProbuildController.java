package com.probuild.controller;

import com.probuild.model.*;
import com.probuild.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
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
        System.out.println("add tool endpoint hit");
        Tool tool = new Tool();
        tool.setName(name);
        tool.setCategory(category);
        tool.setAvailable(true);
        return toolRepository.save(tool);
    }

    // List all tools
    @GetMapping("/tools")
    public Iterable<Tool> listTools() {
        System.out.println("list tools endpoint hit");
        return toolRepository.findAll();
    }

    // Get tool by ID
    @GetMapping("/tools/{id}")
    public Optional<Tool> findToolById(@PathVariable Integer id) {
        System.out.println("find tool by id endpoint hit");
        return toolRepository.findById(id);
    }

    // List only available tools
    @GetMapping("/tools/available")
    public Iterable<Tool> availableTools() {
        System.out.println("available tools endpoint hit");
        return toolRepository.findByAvailableTrue();
    }

    // Update tool availability
    @PutMapping("/tools/{id}/availability")
    public Tool updateAvailability(@PathVariable Integer id,
                                   @RequestParam boolean available) {
        System.out.println("update tool availability endpoint hit");
        Tool tool = toolRepository.findById(id).orElseThrow();
        tool.setAvailable(available);
        return toolRepository.save(tool);
    }

    // ========================
    // Customer Endpoints
    // ========================

    @PostMapping("/customers")
    public Customer addCustomer(@RequestBody Customer customer) {
        System.out.println("add customer endpoint hit");
        return customerRepository.save(customer);
    }

    @GetMapping("/customers")
    public Iterable<Customer> listCustomers() {
        System.out.println("list customers endpoint hit");
        return customerRepository.findAll();
    }

    @GetMapping("/customers/{id}")
    public Optional<Customer> getCustomer(@PathVariable Integer id) {
        System.out.println("get customer by id endpoint hit");
        return customerRepository.findById(id);
    }

    // ========================
    // Booking Endpoints
    // ========================

    @PostMapping("/bookings")
    public Booking addBooking(@RequestBody Booking booking) {
        System.out.println("add booking endpoint hit");
        return bookingRepository.save(booking);
    }

    @GetMapping("/bookings")
    public Iterable<Booking> listBookings() {
        System.out.println("list bookings endpoint hit");
        return bookingRepository.findAll();
    }

    @GetMapping("/bookings/customer/{customerId}")
    public Iterable<Booking> bookingsByCustomer(@PathVariable Integer customerId) {
        System.out.println("bookings by customer endpoint hit");
        return bookingRepository.findByCustomerId(customerId);
    }

    @GetMapping("/bookings/tool/{toolId}")
    public Iterable<Booking> bookingsByTool(@PathVariable Integer toolId) {
        System.out.println("bookings by tool endpoint hit");
        return bookingRepository.findByToolId(toolId);
    }

    // ========================
    // StockRecord Endpoints
    // ========================

    @GetMapping("/stock")
    public Iterable<StockRecord> listStock() {
        System.out.println("list stock endpoint hit");
        return stockRecordRepository.findAll();
    }

    @GetMapping("/stock/tool/{toolId}")
    public Iterable<StockRecord> stockByTool(@PathVariable Integer toolId) {
        System.out.println("stock by tool endpoint hit");
        return stockRecordRepository.findByToolId(toolId);
    }

    // ========================
    // ServiceJob Endpoints
    // ========================

    @GetMapping("/servicejobs")
    public Iterable<ServiceJob> listServiceJobs() {
        System.out.println("list service jobs endpoint hit");
        return serviceJobRepository.findAll();
    }

    @GetMapping("/servicejobs/tool/{toolId}")
    public Iterable<ServiceJob> serviceJobsByTool(@PathVariable Integer toolId) {
        System.out.println("service jobs by tool endpoint hit");
        return serviceJobRepository.findByToolId(toolId);
    }

    // ========================
    // PurchaseOrder Endpoints
    // ========================

    @GetMapping("/purchaseorders")
    public Iterable<PurchaseOrder> listPurchaseOrders() {
        System.out.println("list purchase orders endpoint hit");
        return purchaseOrderRepository.findAll();
    }

    @GetMapping("/purchaseorders/supplier/{supplierName}")
    public Iterable<PurchaseOrder> ordersBySupplier(@PathVariable String supplierName) {
        System.out.println("purchase orders by supplier endpoint hit");
        return purchaseOrderRepository.findBySupplierName(supplierName);
    }

    // ========================
    // Invoice Endpoints
    // ========================

    @GetMapping("/invoices")
    public Iterable<Invoice> listInvoices() {
        System.out.println("list invoices endpoint hit");
        return invoiceRepository.findAll();
    }

    @GetMapping("/invoices/customer/{customerId}")
    public Iterable<Invoice> invoicesByCustomer(@PathVariable Integer customerId) {
        System.out.println("invoices by customer endpoint hit");
        return invoiceRepository.findByCustomerId(customerId);
    }
}