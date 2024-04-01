package com.service.Hotels.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import com.service.Hotels.exceptions.BadRequestException;
import com.service.Hotels.exceptions.NotFoundException;
import com.service.Hotels.models.Hotel;
import com.service.Hotels.models.Notification;
import com.service.Hotels.models.User;
import com.service.Hotels.repositories.UserRepository;
import com.service.Hotels.services.HotelService;
import com.service.Hotels.services.NotificationServiceImp;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationServiceImp service;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private UserRepository userService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getNotificationsByUserId(@PathVariable Long userId) {
        List<Notification> notifications = service.getAllByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<Notification>> getNotificationsByHotelId(@PathVariable Long hotelId) {
        List<Notification> notifications = service.getAllByHotelId(hotelId);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/hotel/{hotelId}")
    public ResponseEntity<Notification> addNotificationToHotel(@PathVariable Long hotelId, @RequestBody Notification notificationDetails) {
        if (hotelId == null) {
            throw new BadRequestException("hotelId cannot be null");
        }
        Hotel hotel = hotelService.findHotelById(hotelId);
        notificationDetails.setHotel(hotel);
        service.add(notificationDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationDetails);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<Notification> addNotificationToUser(@PathVariable Long userId, @RequestBody Notification notificationDetails) {
        if (userId == null) {
            throw new BadRequestException("userId cannot be null");
        }
        User user = userService.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        notificationDetails.setUser(user);
        notificationDetails.setDate(new Date());
        service.add(notificationDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notification> updateNotification(@PathVariable Long id, @RequestBody Notification notificationDetails) {
        Notification notifi = service.findById(id);
        notifi.setIsRead(notificationDetails.getIsRead());
        service.add(notifi);
        return ResponseEntity.ok(notifi);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        service.remove(id);
        return ResponseEntity.noContent().build();
    }
}
