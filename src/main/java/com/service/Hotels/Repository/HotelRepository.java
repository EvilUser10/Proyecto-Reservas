package com.service.Hotels.Repository;

import com.service.Hotels.Model.Hotel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
   List<Hotel> findAllByCity(String city);
}
