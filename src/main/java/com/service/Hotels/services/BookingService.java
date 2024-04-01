package com.service.Hotels.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.Hotels.dto.BookingDto;
import com.service.Hotels.enums.BookingStatus;
import com.service.Hotels.exceptions.BadRequestException;
import com.service.Hotels.exceptions.FieldInvalidException;
import com.service.Hotels.exceptions.NotFoundException;
import com.service.Hotels.models.Booking;
import com.service.Hotels.models.Hotel;
import com.service.Hotels.models.Room;
import com.service.Hotels.models.User;
import com.service.Hotels.repositories.BookingRepository;

@Service
public class BookingService {

  @Autowired
  BookingRepository bookingRepository;

  @Autowired
  HotelService hotelService;
  @Autowired
  UserService userService;
  @Autowired
  RoomService roomService;

  public Booking getBookingById(Long hotelId) {
    if (hotelId == null) {
      throw new IllegalArgumentException("ID can not be null");
    }
    if (bookingRepository.findById(hotelId) == null) {
      throw new NotFoundException("Booking with ID: " + hotelId + "was not found");
    }
    return bookingRepository.findById(hotelId).get();
  }

  public Booking addBooking(Long hotelId, Long userId, BookingDto bookingRequest) {
    LocalDate startDate = convertDate(bookingRequest.getStartDate());
    LocalDate finishDate = convertDate(bookingRequest.getStartDate());

    if (finishDate.isBefore(startDate)) {
      throw new FieldInvalidException("Check-in date must come before check-out date");
    }
    Booking booking = new Booking();
    
    booking.setStartDate(startDate);
    booking.setFinishDate(finishDate);
    User user = userService.getUserById(userId);
    Hotel hotel = hotelService.findHotelById(bookingRequest.getRoomId());
    roomService.makeNotAvailableRoom(bookingRequest.getRoomId());
    booking.setUser(user);
    booking.setHotel(hotel);

    String bookingCode = RandomStringUtils.randomNumeric(10);
    booking.setBookingConfirmationCode(bookingCode);
    booking.setState(BookingStatus.CONFIRMED);

    bookingRepository.save(booking);

    return booking;
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
    if (bookingRepository.findById(id) != null) {
      // Antes de borrar una reserva o despues hay que hacer la modificacion en
      // habitacion, a ponerla como aviable.
      // Get the booking
      // Hote hotel = hotelService.find
      throw new NotFoundException("No booking found");
    }
    try {
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

  public List<Booking> getBookingsByUserEmail(String email) {
    List<Booking> bookings = bookingRepository.findByUserEmail(email);
    if (bookings == null) {
      throw new NotFoundException("No bookings found with email " + email);
    }
    return bookings;
  }

  public List<Booking> getBookingsByUserId(Long userId) {
    return bookingRepository.findByUserId(userId);
  }

  private LocalDate convertDate(String fechaString) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    return LocalDate.parse(fechaString, formatter);
  }
}
