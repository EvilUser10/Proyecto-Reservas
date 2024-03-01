package com.service.Hotels.repositories;

import com.service.Hotels.models.Hotel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
   List<Hotel> findAllByCity(String city);
}
