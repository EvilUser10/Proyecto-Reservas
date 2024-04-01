package com.service.Hotels.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
	private String startDate;
    private String finishDate;
    private Long roomId;
}
