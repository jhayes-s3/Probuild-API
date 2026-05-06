package com.probuild;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.probuild.controller.ProbuildController;
import com.probuild.model.*;
import com.probuild.repository.*;
import com.probuild.scheduled.TradeCardAnnualResetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProbuildController.class)
class ProbuildControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ToolRepository toolRepository;

    @MockitoBean
    private CustomerRepository customerRepository;

    @MockitoBean
    private BookingRepository bookingRepository;

    @MockitoBean
    private StockRecordRepository stockRecordRepository;

    @MockitoBean
    private ServiceJobRepository serviceJobRepository;

    @MockitoBean
    private PurchaseOrderRepository purchaseOrderRepository;

    @MockitoBean
    private InvoiceRepository invoiceRepository;

    @MockitoBean
    private TradeCardRepository tradeCardRepository;

    @MockitoBean
    private TradeCardAnnualResetService tradeCardAnnualResetService;

    @Test
    void addToolCreatesAvailableTool() throws Exception {
        Tool savedTool = new Tool();
        savedTool.setId(1);
        savedTool.setName("Drill");
        savedTool.setCategory("Power Tools");
        savedTool.setAvailable(true);

        when(toolRepository.save(any(Tool.class))).thenReturn(savedTool);

        mockMvc.perform(post("/tools")
                        .param("name", "Drill")
                        .param("category", "Power Tools"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Drill"))
                .andExpect(jsonPath("$.category").value("Power Tools"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void listToolsReturnsTools() throws Exception {
        Tool tool = new Tool();
        tool.setId(1);
        tool.setName("Drill");
        tool.setCategory("Power Tools");
        tool.setAvailable(true);

        when(toolRepository.findAll()).thenReturn(List.of(tool));

        mockMvc.perform(get("/tools"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Drill"));
    }

    @Test
    void availableToolsReturnsOnlyAvailableTools() throws Exception {
        Tool tool = new Tool();
        tool.setId(1);
        tool.setName("Drill");
        tool.setAvailable(true);

        when(toolRepository.findByAvailableTrue()).thenReturn(List.of(tool));

        mockMvc.perform(get("/tools/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].available").value(true));
    }

    @Test
    void updateAvailabilityChangesToolAvailability() throws Exception {
        Tool existingTool = new Tool();
        existingTool.setId(1);
        existingTool.setName("Drill");
        existingTool.setAvailable(true);

        Tool savedTool = new Tool();
        savedTool.setId(1);
        savedTool.setName("Drill");
        savedTool.setAvailable(false);

        when(toolRepository.findById(1)).thenReturn(Optional.of(existingTool));
        when(toolRepository.save(any(Tool.class))).thenReturn(savedTool);

        mockMvc.perform(put("/tools/1/availability")
                        .param("available", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false));
    }

    @Test
    void addCustomerSavesCustomer() throws Exception {
        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("James");
        customer.setEmail("james@example.com");

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        mockMvc.perform(post("/customers")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("James"))
                .andExpect(jsonPath("$.email").value("james@example.com"));
    }

    @Test
    void updateCustomerUpdatesProvidedFieldsOnly() throws Exception {
        Customer existing = new Customer();
        existing.setId(1);
        existing.setName("Old");
        existing.setEmail("old@example.com");

        Customer saved = new Customer();
        saved.setId(1);
        saved.setName("James");
        saved.setEmail("james@example.com");
        saved.setMembershipLevel("Gold");

        when(customerRepository.findById(1)).thenReturn(Optional.of(existing));
        when(customerRepository.save(any(Customer.class))).thenReturn(saved);

        mockMvc.perform(put("/customers/1")
                        .param("name", "James")
                        .param("email", "james@example.com")
                        .param("membershipLevel", "Gold"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("James"))
                .andExpect(jsonPath("$.membershipLevel").value("Gold"));
    }

    @Test
    void addTradeCardCreatesCardWithZeroPoints() throws Exception {
        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("James");

        TradeCard card = new TradeCard();
        card.setId(2);
        card.setCardNumber("CARD-123");
        card.setPointsBalance(0);
        card.setCustomer(customer);

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(tradeCardRepository.save(any(TradeCard.class))).thenReturn(card);

        mockMvc.perform(post("/tradecards")
                        .param("cardNumber", "CARD-123")
                        .param("customerId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNumber").value("CARD-123"))
                .andExpect(jsonPath("$.pointsBalance").value(0));
    }

    @Test
    void addPointsToTradeCardIncreasesBalance() throws Exception {
        TradeCard existing = new TradeCard();
        existing.setId(1);
        existing.setCardNumber("CARD-123");
        existing.setPointsBalance(10);

        TradeCard saved = new TradeCard();
        saved.setId(1);
        saved.setCardNumber("CARD-123");
        saved.setPointsBalance(35);

        when(tradeCardRepository.findById(1)).thenReturn(Optional.of(existing));
        when(tradeCardRepository.save(any(TradeCard.class))).thenReturn(saved);

        mockMvc.perform(post("/tradecards/1/points")
                        .param("pointsToAdd", "25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pointsBalance").value(35));
    }

    @Test
    void resetAllTradeCardsReturnsResetCount() throws Exception {
        when(tradeCardAnnualResetService.resetAll()).thenReturn(3);

        mockMvc.perform(post("/tradecards/reset"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resetCount").value(3));
    }

    @Test
    void addBookingSavesBooking() throws Exception {
        Booking booking = new Booking();
        booking.setId(1);
        booking.setPaymentType("card");
        booking.setDepositAmount(50.00);

        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentType").value("card"))
                .andExpect(jsonPath("$.depositAmount").value(50.00));
    }

    @Test
    void addStockRecordCreatesRecordForTool() throws Exception {
        Tool tool = new Tool();
        tool.setId(1);
        tool.setName("Drill");

        StockRecord record = new StockRecord();
        record.setId(10);
        record.setTool(tool);
        record.setQuantity(5);
        record.setBinLocation("A1");

        when(toolRepository.findById(1)).thenReturn(Optional.of(tool));
        when(stockRecordRepository.save(any(StockRecord.class))).thenReturn(record);

        mockMvc.perform(post("/stock")
                        .param("toolId", "1")
                        .param("quantity", "5")
                        .param("binLocation", "A1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(5))
                .andExpect(jsonPath("$.binLocation").value("A1"));
    }

    @Test
    void addPurchaseOrderCreatesOrder() throws Exception {
        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("James");

        PurchaseOrder order = new PurchaseOrder();
        order.setId(20);
        order.setSupplierName("supplier");
        order.setDeliveryManifest("MAN-123");
        order.setDeliveryAddress("Bristol");
        order.setCustomer(customer);

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(purchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(order);

        mockMvc.perform(post("/purchaseorders")
                        .param("supplierName", "supplier")
                        .param("deliveryManifest", "MAN-123")
                        .param("deliveryAddress", "Bristol")
                        .param("customerId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.supplierName").value("supplier"))
                .andExpect(jsonPath("$.deliveryManifest").value("MAN-123"));
    }

    @Test
    void updatePurchaseOrderUpdatesFields() throws Exception {
        PurchaseOrder existing = new PurchaseOrder();
        existing.setId(20);
        existing.setSupplierName("old");

        TradeCard card = new TradeCard();
        card.setId(5);
        card.setCardNumber("CARD-123");

        PurchaseOrder saved = new PurchaseOrder();
        saved.setId(20);
        saved.setSupplierName("new supplier");
        saved.setDeliveryManifest("MAN-999");
        saved.setTradeCard(card);

        when(purchaseOrderRepository.findById(20)).thenReturn(Optional.of(existing));
        when(tradeCardRepository.findById(5)).thenReturn(Optional.of(card));
        when(purchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(saved);

        mockMvc.perform(put("/purchaseorders/20")
                        .param("supplierName", "new supplier")
                        .param("deliveryManifest", "MAN-999")
                        .param("tradeCardId", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.supplierName").value("new supplier"))
                .andExpect(jsonPath("$.deliveryManifest").value("MAN-999"));
    }

    @Test
    void addInvoiceCreatesInvoice() throws Exception {
        Invoice invoice = new Invoice();
        invoice.setId(1);
        invoice.setInvoiceNumber("INV-123");
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setAmount(250.00);

        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);

        mockMvc.perform(post("/invoices")
                        .param("invoiceNumber", "INV-123")
                        .param("amount", "250.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invoiceNumber").value("INV-123"))
                .andExpect(jsonPath("$.amount").value(250.00));
    }

    @Test
    void addServiceJobCreatesServiceJob() throws Exception {
        Tool tool = new Tool();
        tool.setId(1);
        tool.setName("Drill");

        ServiceJob serviceJob = new ServiceJob();
        serviceJob.setId(1);
        serviceJob.setDescription("Repair Drill");
        serviceJob.setServiceDate(LocalDate.now());
        serviceJob.setServiceLog("Created by BPMN worker");
        serviceJob.setTool(tool);

        when(toolRepository.findById(1)).thenReturn(Optional.of(tool));
        when(serviceJobRepository.save(any(ServiceJob.class))).thenReturn(serviceJob);

        mockMvc.perform(post("/servicejobs")
                        .param("description", "Repair Drill")
                        .param("toolId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Repair Drill"))
                .andExpect(jsonPath("$.serviceLog").value("Created by BPMN worker"));
    }

    @Test
    void listInvoicesReturnsInvoices() throws Exception {
        Invoice invoice = new Invoice();
        invoice.setId(1);
        invoice.setInvoiceNumber("INV-123");
        invoice.setAmount(250.00);

        when(invoiceRepository.findAll()).thenReturn(List.of(invoice));

        mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].invoiceNumber").value("INV-123"));
    }

    @Test
    void listPurchaseOrdersReturnsOrders() throws Exception {
        PurchaseOrder order = new PurchaseOrder();
        order.setId(1);
        order.setSupplierName("supplier");

        when(purchaseOrderRepository.findAll()).thenReturn(List.of(order));

        mockMvc.perform(get("/purchaseorders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].supplierName").value("supplier"));
    }
}