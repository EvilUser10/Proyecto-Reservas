package com.service.Hotels.dto;

import java.time.LocalDate;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
	private Long id;
	
	private LocalDate startDate;

	private LocalDate finishDate;

    private String state;


    private String bookingConfirmationCode;

    private HotelDto room;
    private String guestName ;
    private String guestEmail ;

}
