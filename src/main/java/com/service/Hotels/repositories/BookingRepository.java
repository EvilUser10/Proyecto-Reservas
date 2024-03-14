package com.service.Hotels.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.Hotels.models.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long>{
    Optional<Booking> findByBookingConfirmationCode(String confirmationCode);
    List<Booking> findByGuestEmail(String email);
}
