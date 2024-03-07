package com.service.Hotels.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.Hotels.models.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

  
}
