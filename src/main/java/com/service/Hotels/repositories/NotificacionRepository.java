package com.service.Hotels.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.Hotels.models.Notification;

@Repository
public interface NotificacionRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllNotificationsByUserId(Long id);
    List<Notification> findAllNotificationsByHotelId(Long id);
}
