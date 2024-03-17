package com.service.Hotels.interfaces;

import java.util.List;

import com.service.Hotels.models.Booking;

public interface BookingService {
  Booking getBookingById(Long id);

  String addBooking(Long hotelId, Long userId, Booking bookingRequest);

  void removeBooking(Long id);
  Booking findByBookingConfirmationCode(String confirmationCode);
  List<Booking> getBookingsByUserEmail(String email);
  List<Booking>  getBookingsByUserId(Long userId);
}
