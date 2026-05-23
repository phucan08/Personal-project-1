package com.example.autoparts.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    public static final String METHOD_CASH = "CASH";
    public static final String METHOD_CARD = "CARD";
    public static final String METHOD_BANK_TRANSFER = "BANK_TRANSFER";

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_PAID = "PAID";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    public Payment() {
    }

    public Payment(String paymentMethod, String status, BigDecimal amount) {
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.amount = amount;

        if (STATUS_PAID.equals(status)) {
            this.paidAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;

        if (STATUS_PAID.equals(status)) {
            this.paidAt = LocalDateTime.now();
        }
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }
}
