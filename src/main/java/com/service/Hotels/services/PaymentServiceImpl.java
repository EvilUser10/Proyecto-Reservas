package com.service.Hotels.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.service.Hotels.exceptions.BadRequestException;
import com.service.Hotels.interfaces.PaymentService;
import com.service.Hotels.models.Payment;
import com.service.Hotels.repositories.PaymentRepository;

public class PaymentServiceImpl implements PaymentService{
  @Autowired
  PaymentRepository paymentRepository;

  public Payment getPaymentByID(Long id) {
    return paymentRepository.findById(id).get();
  }

  public Payment addPayment(Payment newPayment) {
    try {
      Payment payment = paymentRepository.save(newPayment);
      return payment;
    } catch (Exception e) {
      throw new BadRequestException("Error adding new payment : " + newPayment);
    }
  }

  public void removePayment(Long id) {
    if (id == null) {
      throw new BadRequestException("The ID cannot be null");
    }
    if (paymentRepository.existsById(id)) {
      try {
        paymentRepository.deleteById(id);
      } catch (IllegalArgumentException ex) {
        throw new BadRequestException("Error deleting the payment with ID " + id);
      }
    }
  }
}
