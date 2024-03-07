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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.Hotels.exceptions.BadRequestException;
import com.service.Hotels.exceptions.NotFoundException;
import com.service.Hotels.models.Booking;
import com.service.Hotels.repositories.BookingRepository;

@RestController
@RequestMapping("/api")
public class BookingController {
    

        @Autowired
        private BookingRepository bookingRepository;

        @GetMapping("/bookings")
        public List<Booking> allBookings() {
            return bookingRepository.findAll();
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

        @GetMapping("/booking/{id}")
        public Booking findUser(@PathVariable("id") Long id) {
            Booking optionalBooking = bookingRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Booking with id: " + id + "not found"));
            return optionalBooking;
        }

        @PostMapping("/booking")
        public Booking createBooking(@RequestBody Booking newBooking) {
            try {
                Booking savedBooking = bookingRepository.save(newBooking);
                return savedBooking;
            } catch (Exception e) {
                throw (new BadRequestException("La reserva no se ha podido crear."));
            }
        }

        @PutMapping("/booking/{id}")
        public ResponseEntity<Booking> editUser(@PathVariable("id") Long id, @RequestBody Booking booking) {

            Booking bookingFind = bookingRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("User with id: " + id + "not found"));

            bookingFind.setFinishDate(booking.getFinishDate());
            bookingFind.setStartDate(booking.getStartDate());
            bookingFind.setState(booking.getState());
            bookingFind.setUser(booking.getUser());
            //bookingFind.setHotel(booking.getHotel());
            // bookingFind.setPayment(booking.getPayment());

            try {
                bookingRepository.save(bookingFind);
                return ResponseEntity.ok(booking);
            } catch (Exception e) {
                throw (new BadRequestException("La reserva no se ha podido modificar."));
            }
        }

        @DeleteMapping("/booking/{id}")
        public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
            if (bookingRepository.existsById(id)) {
                try {
                    bookingRepository.deleteById(id);

                } catch (Exception e) {
                    throw new BadRequestException("La reserva con el id: " + id + " no se ha podido borrar.");
                }
                return ResponseEntity.noContent().build();
            } else {
                throw new NotFoundException("La reserva con el id: " + id + " no se ha encontrado.");
            }
        }
}
