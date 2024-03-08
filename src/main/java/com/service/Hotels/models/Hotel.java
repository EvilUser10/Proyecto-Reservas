package com.service.Hotels.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.util.*;

import com.service.Hotels.exceptions.BadRequestException;

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
    private String email;
    private String description;
    private Double  rating;

    @OneToMany(mappedBy ="hotel")
    List<Room> rooms;

    public void addRooms(Room room) {
        if (rooms == null) {
            rooms = new ArrayList<Room>();
        }
        rooms.add(room);
        room.setHotel(this);
    }

    public void actualizarDisponibilidad() {
        //TODO: implementar la actualización de la disponibilidad del hotel
    }

    public void generarReporte() {
        //TODO: implementar la logica de generar reporte del hotel
    }

    public void gestionarReservas() {
        //TODO: implementar la lógica para gestionar las reservas del hotel
    }
}
