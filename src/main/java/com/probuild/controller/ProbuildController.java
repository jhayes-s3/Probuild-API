package com.probuild.controller;

import com.probuild.model.*;
import com.probuild.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @Autowired
    private TradeCardRepository tradeCardRepository;

    @Autowired
    private com.probuild.scheduled.TradeCardAnnualResetService tradeCardAnnualResetService;

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

    @PutMapping("/customers/{id}")
    public Customer updateCustomer(@PathVariable Integer id,
                                   @RequestParam(required = false) String name,
                                   @RequestParam(required = false) String email,
                                   @RequestParam(required = false) String membershipLevel,
                                   @RequestParam(required = false) String cardNumber,
                                   @RequestParam(required = false) String cardExpiry,
                                   @RequestParam(required = false) String cardCvc) {
        System.out.println("update customer endpoint hit");
        Customer c = customerRepository.findById(id).orElseThrow();
        if (name != null) c.setName(name);
        if (email != null) c.setEmail(email);
        if (membershipLevel != null) c.setMembershipLevel(membershipLevel);
        if (cardNumber != null) c.setCardNumber(cardNumber);
        if (cardExpiry != null) c.setCardExpiry(cardExpiry);
        if (cardCvc != null) c.setCardCvc(cardCvc);
        return customerRepository.save(c);
    }

    // ========================
    // TradeCard Endpoints
    // ========================

    @GetMapping("/tradecards")
    public Iterable<TradeCard> listTradeCards() {
        System.out.println("list trade cards endpoint hit");
        return tradeCardRepository.findAll();
    }

    @GetMapping("/tradecards/customer/{customerId}")
    public Iterable<TradeCard> tradeCardsByCustomer(@PathVariable Integer customerId) {
        System.out.println("trade cards by customer endpoint hit");
        return tradeCardRepository.findByCustomerId(customerId);
    }

    @GetMapping("/tradecards/number/{cardNumber}")
    public Optional<TradeCard> tradeCardByNumber(@PathVariable String cardNumber) {
        System.out.println("trade card by number endpoint hit");
        return tradeCardRepository.findByCardNumber(cardNumber);
    }

    @PostMapping("/tradecards")
    public TradeCard addTradeCard(@RequestParam String cardNumber,
                                  @RequestParam Integer customerId) {
        System.out.println("add trade card endpoint hit");
        TradeCard card = new TradeCard();
        card.setCardNumber(cardNumber);
        card.setIssueDate(LocalDate.now());
        card.setPointsBalance(0);
        customerRepository.findById(customerId).ifPresent(card::setCustomer);
        return tradeCardRepository.save(card);
    }

    @PostMapping("/tradecards/{id}/points")
    public TradeCard addPointsToTradeCard(@PathVariable Integer id,
                                          @RequestParam Integer pointsToAdd) {
        System.out.println("add points to trade card endpoint hit");
        TradeCard card = tradeCardRepository.findById(id).orElseThrow();
        int existing = card.getPointsBalance() == null ? 0 : card.getPointsBalance();
        card.setPointsBalance(existing + pointsToAdd);
        return tradeCardRepository.save(card);
    }

    @PostMapping("/tradecards/reset")
    public java.util.Map<String, Integer> resetAllTradeCards() {
        System.out.println("manual trade card reset endpoint hit");
        int count = tradeCardAnnualResetService.resetAll();
        return java.util.Map.of("resetCount", count);
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

    @PostMapping("/stock")
    public StockRecord addStockRecord(@RequestParam Integer toolId,
                                      @RequestParam Integer quantity,
                                      @RequestParam(required = false) String binLocation) {
        System.out.println("add stock record endpoint hit");
        StockRecord record = new StockRecord();
        toolRepository.findById(toolId).ifPresent(record::setTool);
        record.setQuantity(quantity);
        record.setBinLocation(binLocation);
        return stockRecordRepository.save(record);
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

    @PostMapping("/purchaseorders")
    public PurchaseOrder addPurchaseOrder(@RequestParam String supplierName,
                                          @RequestParam(required = false) String deliveryManifest,
                                          @RequestParam(required = false) String deliveryAddress,
                                          @RequestParam(required = false) Integer customerId) {
        System.out.println("add purchase order endpoint hit");
        PurchaseOrder po = new PurchaseOrder();
        po.setSupplierName(supplierName);
        po.setOrderDate(LocalDate.now());
        po.setExpectedDeliveryDate(LocalDate.now().plusDays(7));
        po.setDeliveryManifest(deliveryManifest);
        po.setDeliveryAddress(deliveryAddress);
        if (customerId != null) {
            customerRepository.findById(customerId).ifPresent(po::setCustomer);
        }
        return purchaseOrderRepository.save(po);
    }

    @PutMapping("/purchaseorders/{id}")
    public PurchaseOrder updatePurchaseOrder(@PathVariable Integer id,
                                             @RequestParam(required = false) String supplierName,
                                             @RequestParam(required = false) String deliveryManifest,
                                             @RequestParam(required = false) String deliveryAddress,
                                             @RequestParam(required = false) Integer tradeCardId) {
        System.out.println("update purchase order endpoint hit");
        PurchaseOrder po = purchaseOrderRepository.findById(id).orElseThrow();
        if (supplierName != null) po.setSupplierName(supplierName);
        if (deliveryManifest != null) po.setDeliveryManifest(deliveryManifest);
        if (deliveryAddress != null) po.setDeliveryAddress(deliveryAddress);
        if (tradeCardId != null) {
            tradeCardRepository.findById(tradeCardId).ifPresent(po::setTradeCard);
        }
        return purchaseOrderRepository.save(po);
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

    @PostMapping("/invoices")
    public Invoice addInvoice(@RequestParam String invoiceNumber,
                              @RequestParam Double amount,
                              @RequestParam(required = false) Integer customerId) {
        System.out.println("add invoice endpoint hit");
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setAmount(amount);
        if (customerId != null) {
            customerRepository.findById(customerId).ifPresent(invoice::setCustomer);
        }
        return invoiceRepository.save(invoice);
    }

    // ========================
    // ServiceJob extra endpoints
    // ========================

    @PostMapping("/servicejobs")
    public ServiceJob addServiceJob(@RequestParam String description,
                                    @RequestParam(required = false) Integer toolId) {
        System.out.println("add service job endpoint hit");
        ServiceJob job = new ServiceJob();
        job.setDescription(description);
        job.setServiceDate(LocalDate.now());
        job.setServiceLog("Created by BPMN worker");
        if (toolId != null) {
            toolRepository.findById(toolId).ifPresent(job::setTool);
        }
        return serviceJobRepository.save(job);
    }
}