package com.service.Hotels.dto;

import java.time.LocalDate;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
	private LocalDate startDate;
    private LocalDate finishDate;
    private Long roomId;
}
