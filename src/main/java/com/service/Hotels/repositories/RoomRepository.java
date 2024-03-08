package com.service.Hotels.repositories;


import org.springframework.stereotype.Repository;

import com.service.Hotels.models.Room;


@Repository
public interface RoomRepository extends GenericRepository<Room, Long> {
}
