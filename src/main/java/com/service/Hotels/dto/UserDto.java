package com.service.Hotels.dto;

import com.service.Hotels.interfaces.Role;
import com.service.Hotels.models.Booking;
import com.service.Hotels.models.Notification;
import com.service.Hotels.models.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    Role role;
	private String name;

    private Long id;

	private String username;

	private String email;

	private List<Booking> bookings;

    private List<Notification> notifications;

	public UserDto(User user) {
        this.setId(user.getId());
        this.setRole(user.getRole());
        this.setName(user.getName());
        this.setUsername(user.getUsername());
        this.setEmail(user.getEmail());
        this.setBookings(user.getBookings());
        this.setNotifications(user.getNotifications());
    }

}
