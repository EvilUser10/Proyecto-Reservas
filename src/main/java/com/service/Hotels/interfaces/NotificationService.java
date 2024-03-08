package com.service.Hotels.interfaces;

import com.service.Hotels.models.Notification;

import java.util.List;
public interface NotificationService {
    List<Notification> getAllByUserId(Long id);
    List<Notification> getAllByHotelId(Long id);
    List<Notification> getAll();
    Notification findById(Long id);
    Notification add(Notification newNotification);
    void remove(Long id);
}
