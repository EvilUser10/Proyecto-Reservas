package com.service.Hotels.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.service.Hotels.exceptions.BadRequestException;
import com.service.Hotels.interfaces.BookingService;
import com.service.Hotels.models.Booking;
import com.service.Hotels.repositories.BookingRepository;

public class BookingServiceImpl implements BookingService{

  @Autowired
  BookingRepository bookingRepository;

  public Booking getBookingByID(Long id) {
    return bookingRepository.findById(id).get();
  }

  public Booking addBooking(Booking newBooking) {
    try {
      Booking booking = bookingRepository.save(newBooking);
      return booking;
    } catch (Exception e) {
      throw new BadRequestException("Error adding new booking : " + newBooking);
    }
  }

  public void removeBooking(Long id) {
    if (id == null) {
      throw new BadRequestException("The ID cannot be null");
    }
    if (bookingRepository.existsById(id)) {
      try {
        bookingRepository.deleteById(id);
      } catch (IllegalArgumentException ex) {
        throw new BadRequestException("Error deleting the booking with ID " + id);
      }
    }
  }
}
