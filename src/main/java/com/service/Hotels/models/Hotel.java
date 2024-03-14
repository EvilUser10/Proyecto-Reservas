package com.service.Hotels.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.util.*;

import org.apache.commons.lang3.RandomStringUtils;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String city;

    @ElementCollection
    @CollectionTable(name = "hotel_fotos", joinColumns = @JoinColumn(name = "hotel_id"))
    @Column(columnDefinition = "TEXT")
    private List<String> fotos;

    private String phone;
    private Double latitud;
    private Double longitud;
    private String email;
    private String description;
    private Double  rating;

    @OneToMany(mappedBy ="hotel", cascade = CascadeType.ALL)
    private List<Room> rooms;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<Rating> ratings;

    public void actualizarDisponibilidad() {
        //TODO: implementar la actualización de la disponibilidad del hotel
    }

    public void gestionarReservas() {
        //TODO: implementar la lógica para gestionar las reservas del hotel
    }
}
