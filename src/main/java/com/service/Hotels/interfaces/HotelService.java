package com.service.Hotels.interfaces;

import com.service.Hotels.models.Hotel;

import java.util.List;



public interface HotelService {
    List<Hotel> getAllHotelsByCity(String city);
    Hotel findHotelById(Long id);
    Hotel addHotel(Hotel newHotel);
    void removeHotel(Long id);
}
