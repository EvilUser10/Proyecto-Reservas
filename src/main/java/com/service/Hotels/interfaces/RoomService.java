package com.service.Hotels.interfaces;


import java.util.*;

import com.service.Hotels.models.Room;

public interface RoomService {
    List<Room> getAllByHotelId(Long id);
    Room findById(Long id);
    Room add(Room newRoom);
    Room update(Room newRoom);
    void remove(Long id);
}
