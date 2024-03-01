package com.service.Hotels.models;
import lombok.Data;
import jakarta.persistence.*;

import java.util.List;

@Data
@Entity
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
