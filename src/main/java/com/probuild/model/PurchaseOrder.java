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

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }

    public LocalDate getExpectedDeliveryDate() { return expectedDeliveryDate; }
    public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) { this.expectedDeliveryDate = expectedDeliveryDate; }

    public String getDeliveryManifest() { return deliveryManifest; }
    public void setDeliveryManifest(String deliveryManifest) { this.deliveryManifest = deliveryManifest; }
}