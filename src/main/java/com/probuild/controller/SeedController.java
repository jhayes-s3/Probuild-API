package com.probuild.controller;

import com.probuild.model.*;
import com.probuild.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demo seed: hit POST /seed to populate the DB with a realistic slice
 * (customers, tools, stock, trade cards across 3 discount tiers, past
 * orders linked back to cards, invoices, service jobs). Idempotent-ish:
 * each call adds another batch — run it once for clean demo state.
 */
@RestController
public class SeedController {

    @Autowired private CustomerRepository customerRepository;
    @Autowired private ToolRepository toolRepository;
    @Autowired private StockRecordRepository stockRecordRepository;
    @Autowired private TradeCardRepository tradeCardRepository;
    @Autowired private PurchaseOrderRepository purchaseOrderRepository;
    @Autowired private InvoiceRepository invoiceRepository;
    @Autowired private ServiceJobRepository serviceJobRepository;
    @Autowired private BookingRepository bookingRepository;

    @PostMapping("/seed")
    public Map<String, Integer> seed() {
        System.out.println("seed endpoint hit");

        // --- Customers (mix of regulars + new) ---
        Customer alice = saveCustomer("Alice Johnson", "alice@example.com", "standard");
        Customer bob = saveCustomer("Bob Singh", "bob.singh@buildco.uk", "standard");
        Customer carol = saveCustomer("Carol Mendez", "carol@buildersrus.co.uk", "standard");
        Customer dan = saveCustomer("Dan O'Reilly", "dan@oreillyandsons.uk", "standard");
        Customer eve = saveCustomer("Eve Patel", "eve.patel@example.com", null);

        // --- Tools (mix of categories + availability) ---
        Tool drill = saveTool("Cordless Drill", "Power Tools", true);
        Tool saw = saveTool("Circular Saw", "Power Tools", true);
        Tool sander = saveTool("Belt Sander", "Power Tools", false);
        Tool mixer = saveTool("Cement Mixer", "Heavy Equipment", true);
        Tool washer = saveTool("Pressure Washer", "Outdoor Equipment", true);
        Tool ladder = saveTool("Extension Ladder", "Access Equipment", true);
        Tool jackhammer = saveTool("Jackhammer", "Heavy Equipment", false);

        // --- Stock records ---
        saveStock(drill, 12, "A1-B2-L1-P3");
        saveStock(saw, 8, "A1-B3-L1-P1");
        saveStock(sander, 0, "A2-B1-L2-P4");
        saveStock(mixer, 3, "B1-B1-L1-P1");
        saveStock(washer, 5, "A3-B2-L1-P2");
        saveStock(ladder, 15, "C1-B1-L1-P1");
        saveStock(jackhammer, 2, "B2-B1-L2-P1");

        // --- Trade cards across all 3 discount tiers ---
        TradeCard aliceCard = saveTradeCard(alice, "TC-2025-0001", 1850); // mid tier 5%
        TradeCard bobCard = saveTradeCard(bob, "TC-2025-0002", 240);     // entry tier 2%
        TradeCard carolCard = saveTradeCard(carol, "TC-2025-0003", 3420); // top tier 10%
        TradeCard danCard = saveTradeCard(dan, "TC-2025-0004", 760);     // mid tier 5%
        // Eve has no trade card

        // --- Past purchase orders linked to customers + cards ---
        savePurchaseOrder("BuildCo Supplies", "Order #2025-0042", "12 Highbury Road, London", alice, aliceCard);
        savePurchaseOrder("BuildCo Supplies", "Order #2025-0043", "47 Crown Street, Manchester", bob, bobCard);
        savePurchaseOrder("Trade Direct UK", "Order #2025-0044", "9 Woodland Crescent, Bristol", carol, carolCard);
        savePurchaseOrder("BuildCo Supplies", "Order #2025-0045", "Unit 3, Riverside Park, Leeds", dan, danCard);
        savePurchaseOrder("Heavy Tools Ltd", "Order #2025-0046", "88 Park Lane, Birmingham", eve, null);

        // --- Invoices ---
        saveInvoice("INV-2025-0042", 245.00, alice);
        saveInvoice("INV-2025-0043", 87.50, bob);
        saveInvoice("INV-2025-0044", 1290.00, carol);
        saveInvoice("INV-2025-0045", 320.00, dan);

        // --- Service jobs (pre-existing maintenance history) ---
        saveServiceJob("Annual safety inspection", drill);
        saveServiceJob("Belt replacement", sander);
        saveServiceJob("Hydraulic seal replacement", jackhammer);

        // --- Bookings (a few active rentals) ---
        saveBooking(alice, drill, 50.00, "card");
        saveBooking(bob, saw, 75.00, "card");
        saveBooking(carol, mixer, 200.00, "membership");

        Map<String, Integer> counts = new HashMap<>();
        counts.put("customers", (int) customerRepository.count());
        counts.put("tools", (int) toolRepository.count());
        counts.put("stockRecords", (int) stockRecordRepository.count());
        counts.put("tradeCards", (int) tradeCardRepository.count());
        counts.put("purchaseOrders", (int) purchaseOrderRepository.count());
        counts.put("invoices", (int) invoiceRepository.count());
        counts.put("serviceJobs", (int) serviceJobRepository.count());
        counts.put("bookings", (int) bookingRepository.count());
        return counts;
    }

    // --- Helpers ---

    private Customer saveCustomer(String name, String email, String membershipLevel) {
        Customer c = new Customer();
        c.setName(name);
        c.setEmail(email);
        c.setMembershipLevel(membershipLevel);
        return customerRepository.save(c);
    }

    private Tool saveTool(String name, String category, boolean available) {
        Tool t = new Tool();
        t.setName(name);
        t.setCategory(category);
        t.setAvailable(available);
        return toolRepository.save(t);
    }

    private StockRecord saveStock(Tool tool, int qty, String bin) {
        StockRecord s = new StockRecord();
        s.setTool(tool);
        s.setQuantity(qty);
        s.setBinLocation(bin);
        return stockRecordRepository.save(s);
    }

    private TradeCard saveTradeCard(Customer customer, String number, int points) {
        TradeCard tc = new TradeCard();
        tc.setCustomer(customer);
        tc.setCardNumber(number);
        tc.setPointsBalance(points);
        tc.setIssueDate(LocalDate.now().minusMonths(8));
        return tradeCardRepository.save(tc);
    }

    private PurchaseOrder savePurchaseOrder(String supplier, String manifest, String address,
                                            Customer customer, TradeCard tradeCard) {
        PurchaseOrder po = new PurchaseOrder();
        po.setSupplierName(supplier);
        po.setOrderDate(LocalDate.now().minusDays((int) (Math.random() * 60)));
        po.setExpectedDeliveryDate(LocalDate.now().plusDays(7));
        po.setDeliveryManifest(manifest);
        po.setDeliveryAddress(address);
        po.setCustomer(customer);
        po.setTradeCard(tradeCard);
        return purchaseOrderRepository.save(po);
    }

    private Invoice saveInvoice(String number, double amount, Customer customer) {
        Invoice inv = new Invoice();
        inv.setInvoiceNumber(number);
        inv.setAmount(amount);
        inv.setInvoiceDate(LocalDate.now().minusDays((int) (Math.random() * 30)));
        inv.setCustomer(customer);
        return invoiceRepository.save(inv);
    }

    private ServiceJob saveServiceJob(String description, Tool tool) {
        ServiceJob sj = new ServiceJob();
        sj.setDescription(description);
        sj.setServiceDate(LocalDate.now().minusDays((int) (Math.random() * 90)));
        sj.setServiceLog("Completed by Fixpro");
        sj.setTool(tool);
        return serviceJobRepository.save(sj);
    }

    private Booking saveBooking(Customer customer, Tool tool, double deposit, String paymentType) {
        Booking b = new Booking();
        b.setCustomer(customer);
        b.setTool(tool);
        b.setStartDate(LocalDate.now().minusDays(2));
        b.setEndDate(LocalDate.now().plusDays(5));
        b.setDepositAmount(deposit);
        b.setPaymentType(paymentType);
        return bookingRepository.save(b);
    }
}
