package com.service.Hotels.dto;

import java.time.LocalDate;

import com.service.Hotels.enums.BookingStatus;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {

	private LocalDate startDate;

	private LocalDate finishDate;

    private String hotel_id;
    private String guestName ;
    private String guestEmail ;

}
