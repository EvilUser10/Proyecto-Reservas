package com.service.Hotels.interfaces;

import com.service.Hotels.models.Booking;

public interface BookingService {
  Booking getBookingByID(Long id);

  Booking addBooking(Booking newBooking);

  void removeBooking(Long id);
}
