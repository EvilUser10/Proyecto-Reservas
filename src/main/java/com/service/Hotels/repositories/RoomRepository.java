package com.service.Hotels.repositories;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.service.Hotels.models.Room;


@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findAllRoomsByHotelId(Long id);
}
