package com.service.Hotels.services;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.Hotels.enums.BookingStatus;
import com.service.Hotels.exceptions.BadRequestException;
import com.service.Hotels.exceptions.FieldInvalidException;
import com.service.Hotels.exceptions.NotFoundException;
import com.service.Hotels.interfaces.BookingService;
import com.service.Hotels.models.Booking;
import com.service.Hotels.models.Hotel;
import com.service.Hotels.models.Room;
import com.service.Hotels.models.User;
import com.service.Hotels.repositories.BookingRepository;

@Service
public class BookingServiceImpl implements BookingService {

  @Autowired
  BookingRepository bookingRepository;

  @Autowired
  HotelServiceImpl hotelService;
  @Autowired
  UserServiceImpl userService;

  public Booking getBookingById(Long id) {
    return bookingRepository.findById(id).get();
  }

  @Override
  public String addBooking(Long hotelId, Long userId, Booking bookingRequest) {
    if (bookingRequest.getFinishDate().isBefore(bookingRequest.getStartDate())) {
      throw new FieldInvalidException("Check-in date must come before check-out date");
    }
    Hotel hotel = hotelService.findHotelById(hotelId);
    User user = userService.getUserById(userId);

    List<Room> rooms = hotel.getRooms();

    Room roomAvailable = bookRoom(rooms);
    if (roomAvailable != null) {
      roomAvailable.setAvailable(false);
    }
    bookingRequest.setUser(user);
    bookingRequest.setHotel(hotel);

    String bookingCode = RandomStringUtils.randomNumeric(10);
    bookingRequest.setBookingConfirmationCode(bookingCode);
    bookingRequest.setState(BookingStatus.CONFIRMED);


    bookingRepository.save(bookingRequest);

    return bookingRequest.getBookingConfirmationCode();
  }

  private Room bookRoom(List<Room> rooms) {
    return rooms.stream()
        .filter(room -> room.getAvailable())
        .findAny()
        .orElse(null);
  }

  public void removeBooking(Long id) {
    if (id == null) {
      throw new BadRequestException("The ID cannot be null");
    }
    Booking booking = bookingRepository.findById(id).orElseThrow(() -> new NotFoundException("No booking found"));
      try {
        //Antes de borrar una reserva o despues hay que hacer la modificacion en habitacion, a ponerla como aviable.
        //Get the booking
        //Hote hotel = hotelService.find
        
        bookingRepository.deleteById(id);
      } catch (IllegalArgumentException ex) {
        throw new BadRequestException("Error deleting the booking with ID " + id);
      }
  }

  public Booking findByBookingConfirmationCode(String confirmationCode) {
    return bookingRepository.findByBookingConfirmationCode(confirmationCode)
        .orElseThrow(() -> new NotFoundException("No booking found with booking code :" + confirmationCode));
  }

  public List<Booking> getAllBookings() {
    return bookingRepository.findAll();
  }

  @Override
  public List<Booking> getBookingsByUserEmail(String email) {
    return bookingRepository.findByUserEmail(email);
  }

  @Override
  public List<Booking> getBookingsByUserId(Long userId) {
    return bookingRepository.findByUserId(userId);
   
  }


 
}
