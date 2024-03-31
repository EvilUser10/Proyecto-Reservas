package com.service.Hotels.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.service.Hotels.exceptions.BadRequestException;
import com.service.Hotels.exceptions.NotFoundException;
import com.service.Hotels.models.Payment;
import com.service.Hotels.repositories.PaymentRepository;

@Service
public class PaymentService {
  @Autowired
  PaymentRepository paymentRepository;

  public Payment getPaymentByID(Long id) {
    if (id == null) {
      throw new IllegalArgumentException("ID can not be null");
    }
    if (paymentRepository.findById(id) == null) {
      throw new NotFoundException("Booking with ID: " + id + "was not found");
    }
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

  public void deletePayment(Long id) {
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

  public Payment editPayment(Long id, Payment payment) {
    Payment paymentFind = paymentRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Payment with id: " + id + "not found"));

    paymentFind.setCuantity(payment.getCuantity());
    paymentFind.setPaymentMethod(payment.getPaymentMethod());
    paymentFind.setDate(payment.getDate());
    paymentFind.setState(payment.getState());

    try {
      return paymentRepository.save(paymentFind);
    } catch (Exception e) {
      throw (new BadRequestException("The payment has couldn't been modified"));
    }
  }

}
