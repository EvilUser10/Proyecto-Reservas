package com.service.Hotels.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.service.Hotels.models.Booking;
import com.service.Hotels.models.Booking;
import com.service.Hotels.repositories.BookingRepository;
import com.service.Hotels.repositories.BookingRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
public class BookingController {
  @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/bookings")
    public List<Booking> allBookings() {
        return bookingRepository.findAll();
    }

    @GetMapping("/booking/{id}")
    public ResponseEntity<?> findBooking(@PathVariable("id") Long id) {
        Optional<Booking> optionalBooking = bookingRepository.findById(id);
        if (optionalBooking.isPresent()) {
            Booking Booking = optionalBooking.get();
            return ResponseEntity.ok(Booking);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "booking not found");
            errorResponse.put("status_code", HttpStatus.NOT_FOUND.value()); 
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @PostMapping("/Booking")
    public ResponseEntity<Booking> createBooking(@RequestBody Booking newBooking) {

        Booking savedBooking = bookingRepository.save(newBooking);

        if (savedBooking != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBooking);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/booking/{id}")
    public ResponseEntity<Booking> editBooking(@PathVariable("id") Long id, @RequestBody Booking Booking) {
        Booking editedBooking = bookingRepository.save(Booking);

        if (editedBooking != null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(editedBooking);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }

    @DeleteMapping("/Booking/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable("id") Long id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // Eliminación exitosa
        } else {
            return ResponseEntity.notFound().build(); // Usuario no encontrado
        }
    }
}
