package com.service.Hotels.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.service.Hotels.models.Payment;
import com.service.Hotels.repositories.PaymentRepository;
import com.service.Hotels.services.PaymentService;

@RestController
@RequestMapping("/api")
public class PaymentController {
    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    PaymentService paymentService;

    @GetMapping("/payment")
    public ResponseEntity<?> allPayments() {
        return ResponseEntity.ok(paymentRepository.findAll());
    }

    @GetMapping("/payment/{id}")
    public ResponseEntity<Payment> findPayment(@PathVariable("id") Long id) {
        return ResponseEntity.ok(paymentService.getPaymentByID(id));
    }

    @PostMapping("/payment")
    public ResponseEntity<Payment> createPayment(@RequestBody Payment newPayment) {
        return ResponseEntity.ok(paymentService.addPayment(newPayment));
    }

    @PutMapping("/payment/{id}")
    public ResponseEntity<Payment> editPayment(@PathVariable("id") Long id, @RequestBody Payment payment) {
        return ResponseEntity.ok(paymentService.editPayment(id, payment));
    }

    @DeleteMapping("/payment/{id}")
    public ResponseEntity<String> deletePayment(@PathVariable("id") Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.ok("Pago con id" + id + "borrado correctamente");
    }
}
