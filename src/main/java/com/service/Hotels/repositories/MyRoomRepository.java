package com.service.Hotels.repositories;

import com.service.Hotels.models.Hotel;
import com.service.Hotels.models.Room;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyRoomRepository extends JpaRepository<Room, Long> {
   
}
