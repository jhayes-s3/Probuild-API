package com.probuild.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String supplierName;
    private LocalDate orderDate;
    private LocalDate expectedDeliveryDate;
    private String deliveryManifest;
    private String deliveryAddress;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private TradeCard tradeCard;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }

    public LocalDate getExpectedDeliveryDate() { return expectedDeliveryDate; }
    public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) { this.expectedDeliveryDate = expectedDeliveryDate; }

    public String getDeliveryManifest() { return deliveryManifest; }
    public void setDeliveryManifest(String deliveryManifest) { this.deliveryManifest = deliveryManifest; }

    public TradeCard getTradeCard() { return tradeCard; }
    public void setTradeCard(TradeCard tradeCard) { this.tradeCard = tradeCard; }
}