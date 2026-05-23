package com.example.autoparts.repository;

import com.example.autoparts.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
}
