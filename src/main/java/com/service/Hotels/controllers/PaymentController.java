package com.service.Hotels.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.service.Hotels.exceptions.BadRequestException;
import com.service.Hotels.exceptions.NotFoundException;
import com.service.Hotels.models.Payment;
import com.service.Hotels.repositories.PaymentRepository;

@RestController
public class PaymentController {
  @Autowired
  PaymentRepository paymentRepository;

 @GetMapping("/payment")
    public List<Payment> allPayments() {
        return paymentRepository.findAll();
    }

    // @GetMapping("/user/{id}")
    // public ResponseEntity<?> findUser(@PathVariable("id") Long id) {
    // Optional<User> optionalUser = userRepository.findById(id);
    // if (optionalUser.isPresent()) {
    // User user = optionalUser.get();
    // return ResponseEntity.ok(user);
    // } else {
    // return ResponseEntity.notFound().build();
    // }
    // }

    @GetMapping("/payment/{id}")
    public Payment findPayment(@PathVariable("id") Long id) {
      Payment optionalPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment with id: " + id + "not found"));
        return optionalPayment;
    }

    @PostMapping("/payment")
    public Payment createPayment(@RequestBody Payment newPayment) {
        try {
            Payment savedPayment = paymentRepository.save(newPayment);
            return savedPayment;
        } catch (Exception e) {
            throw (new BadRequestException("The payment has couldn't been made"));
        }
    }

    @PutMapping("/payment/{id}")
    public ResponseEntity<Payment> editPayment(@PathVariable("id") Long id, @RequestBody Payment payment) {

        Payment paymentFind = paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment with id: " + id + "not found"));

                paymentFind.setCuantity(payment.getCuantity());
                paymentFind.setPaymentMethod(payment.getPaymentMethod());
                paymentFind.setDate(payment.getDate());
                paymentFind.setState(payment.getState());

        try {
          paymentRepository.save(paymentFind);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            throw (new BadRequestException("The payment has couldn't been modified"));
        }
    }

    @DeleteMapping("/payment/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        if (paymentRepository.existsById(id)) {
            try {
              paymentRepository.deleteById(id);

            } catch (Exception e) {
                throw new BadRequestException("The payment with id: " + id + " couldn't be deleted");
            }
            return ResponseEntity.noContent().build();
        } else {
            throw new NotFoundException("The payment with id: " + id + " wasn't found");
        }
    }
}
