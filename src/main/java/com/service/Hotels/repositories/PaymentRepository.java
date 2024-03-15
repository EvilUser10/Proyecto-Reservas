package com.service.Hotels.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.Hotels.models.Payment;
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{

  
}
