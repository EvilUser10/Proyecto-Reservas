package com.service.Hotels.dto;

import java.util.List;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDto {
    private Long id;
    private String name;
    private String address;
    private String city;
    private List<String> fotos;
    private String phone;
    private Double latitud;
    private Double longitud;
    private String email;
    private String description;
    private Double  rating;
}
