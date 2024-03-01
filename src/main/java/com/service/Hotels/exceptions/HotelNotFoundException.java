package com.service.Hotels.exceptions;

public class HotelNotFoundException extends RuntimeException  {
    public HotelNotFoundException() {
        super("No se encuentra ningún hotel");
    }

    public HotelNotFoundException(String city) {
        super("No se encuentra ningún hotel " +
                (city != null ? "en la ciudad de '" + city + "'" : ""));
    }

    public HotelNotFoundException(Long id) {
        super("No se encuentra ningún hotel con el ID " +
                (id != null ? id : ""));
    }

    public HotelNotFoundException(String city, Long id) {
        super("No se encuentra ningún hotel " +
                (city != null ? "en la ciudad de '" + city + "'" : "") +
                (id != null ? (city != null ? " y" : "") + " con el ID " + id : ""));
    }
}
