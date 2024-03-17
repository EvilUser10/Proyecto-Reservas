package com.service.Hotels.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.Hotels.models.Booking;
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>{
    Optional<Booking> findByBookingConfirmationCode(String confirmationCode);
    List<Booking> findByUserEmail(String email);
    List<Booking>  findByUserId(Long userId);
}
