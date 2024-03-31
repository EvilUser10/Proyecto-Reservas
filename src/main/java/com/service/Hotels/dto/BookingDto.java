package com.service.Hotels.dto;

import java.time.LocalDate;

import com.service.Hotels.enums.BookingStatus;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
	private String startDate;
    private String finishDate;
}
