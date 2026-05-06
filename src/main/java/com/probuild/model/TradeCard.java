package com.probuild.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class TradeCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String cardNumber;
    private Integer pointsBalance;
    private LocalDate issueDate;

    @ManyToOne
    private Customer customer;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public Integer getPointsBalance() { return pointsBalance; }
    public void setPointsBalance(Integer pointsBalance) { this.pointsBalance = pointsBalance; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
}
