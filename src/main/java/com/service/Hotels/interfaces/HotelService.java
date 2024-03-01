package com.service.Hotels.interfaces;

import com.service.Hotels.Model.Hotel;

import java.util.List;


public interface HotelService {
    List<Hotel> getAllHotelsByCity(String city);
    Hotel findHotelById(Long id);
}
