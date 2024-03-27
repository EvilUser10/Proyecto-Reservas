package com.service.Hotels.dto;

import java.time.LocalDate;

import com.service.Hotels.enums.BookingStatus;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
	private Long id;
	
	private LocalDate startDate;

	private LocalDate finishDate;

    private BookingStatus state;

    private String bookingConfirmationCode;

    private HotelDto hotel;
    private String guestName ;
    private String guestEmail ;

}
