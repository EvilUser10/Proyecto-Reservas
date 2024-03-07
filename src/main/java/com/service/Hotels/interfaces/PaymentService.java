package com.service.Hotels.interfaces;

import com.service.Hotels.models.Payment;

public interface PaymentService {
  Payment getPaymentByID(Long id);
  Payment addPayment(Payment newPayment);
  void removePayment(Long id);
}
