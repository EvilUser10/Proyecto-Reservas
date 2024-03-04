package com.service.Hotels.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.Hotels.models.Hotel;
import com.service.Hotels.assemblers.HotelModelAssembler;
import com.service.Hotels.interfaces.HotelService;
import com.service.Hotels.exceptions.NotFoundException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api")
public class HotelController {

    @Autowired
    private HotelService hotelService;
    @Autowired
    private HotelModelAssembler assembler;


    @GetMapping("/hotels/{city}")
    public CollectionModel<EntityModel<Hotel>> getAll(@PathVariable String city) {
        List<Hotel> hotels = hotelService.getAllHotelsByCity(city);
        if (hotels.isEmpty()) {
            throw new NotFoundException("No se encuentra ningún hotel en la ciudad de "+ city);
        }
        List<EntityModel<Hotel>> hotelModels = hotels.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(hotelModels, linkTo(methodOn(HotelController.class).getAll(city)).withSelfRel());

    }

    @GetMapping("/hotels/{city}/{id}")
    public EntityModel<Hotel> getHotel(@PathVariable String city, @PathVariable Long id) {

        // Obtener la lista de hoteles en la ciudad dada
        List<Hotel> hotels = hotelService.getAllHotelsByCity(city);
        if(hotels.size() <= 0){
            throw new NotFoundException("No se encuentra ningún hotel en la ciudad de + " + city);
        }

        // Buscar el hotel específico por su ID
        Hotel hotel = hotels.stream()
                .filter(h -> h.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No se encuentra ningún hotel en la ciudad de " + city + " con el ID " + id ));

        // Convertir el hotel encontrado en un EntityModel
        return assembler.toModel(hotel);
    }
}
