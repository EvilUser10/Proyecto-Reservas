package com.service.Hotels.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.Hotels.dto.BookingDto;
import com.service.Hotels.dto.HotelDto;
import com.service.Hotels.exceptions.BadRequestException;
import com.service.Hotels.exceptions.NotFoundException;
import com.service.Hotels.models.Booking;
import com.service.Hotels.models.Hotel;
import com.service.Hotels.models.Room;
import com.service.Hotels.models.User;
import com.service.Hotels.repositories.BookingRepository;
import com.service.Hotels.services.BookingServiceImpl;
import com.service.Hotels.services.HotelServiceImpl;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingServiceImpl bookingService;

    @Autowired
    private HotelServiceImpl hotelService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingDto>> allBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        List<BookingDto> bookingResponses = new ArrayList<>();
        for (Booking booking : bookings){
            BookingDto bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
        //return bookingRepository.findAll();
    }

    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
        try {
            Booking booking = bookingService.findByBookingConfirmationCode(confirmationCode);
            BookingDto bookingResponse = getBookingResponse(booking);
            return ResponseEntity.ok(bookingResponse);
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/hotel/{hotelId}/booking")
    public ResponseEntity<?> createBooking(@PathVariable Long hotelId, @RequestBody Booking bookingRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long userId = ((User) authentication.getPrincipal()).getId();

            String confirmationCode = bookingService.addBooking(hotelId, userId, bookingRequest);
            return ResponseEntity.ok(
                    "The hotel booked successfully, Your booking confirmation code is :" + confirmationCode);
        } catch (Exception e) {
            throw (new BadRequestException("La reserva no se ha podido crear."));
        }
    }


    @GetMapping("/user/{email}/bookings")
    public ResponseEntity<List<BookingDto>> getBookingsByUserEmail(@PathVariable String email) {
        List<Booking> bookings = bookingService.getBookingsByUserEmail(email);
        List<BookingDto> bookingResponses = new ArrayList<>();
        for (Booking booking : bookings) {
            BookingDto bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }

    @GetMapping("/user/{userId}/bookings")
    public ResponseEntity<List<BookingDto>> getBookingsByUserId(@PathVariable Long userId) {
        List<Booking> bookings = bookingService.getBookingsByUserId(userId);
        List<BookingDto> bookingResponses = new ArrayList<>();
        for (Booking booking : bookings) {
            BookingDto bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }

    @DeleteMapping("/{bookingId}/cancel")
    public ResponseEntity<Void> deleteBooking(@PathVariable("id") Long id) {
        bookingService.removeBooking(id);
        return ResponseEntity.noContent().build();
    }


    private BookingDto getBookingResponse(Booking booking) {
        Hotel theHotel = hotelService.findHotelById(booking.getHotel().getId());
        HotelDto hotel = new HotelDto(
            theHotel.getId(),
        theHotel.getName(),
        theHotel.getAddress(),
        theHotel.getCity(),
        theHotel.getFotos(),
        theHotel.getPhone(),
        theHotel.getLatitud(), 
        theHotel.getLongitud(), 
        theHotel.getEmail(),
        theHotel.getDescription(),
        theHotel.getRating()
        );
        return new BookingDto(booking.getId(),
        booking.getStartDate(),
        booking.getFinishDate(),
        booking.getState(),
        booking.getBookingConfirmationCode(),
        hotel,
        booking.getUser().getName() == null ? booking.getUser().getUsername() :booking.getUser().getName(),
        booking.getUser().getEmail());
    }

     
}
