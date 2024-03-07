package com.service.Hotels.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.Hotels.models.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long>{
    
}