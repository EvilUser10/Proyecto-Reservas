package com.service.Hotels.controllers;

import java.util.List;
import java.util.stream.Collectors;


import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.Hotels.models.Hotel;
import com.service.Hotels.services.HotelServiceImpl;
import com.service.Hotels.assemblers.HotelModelAssembler;
import com.service.Hotels.exceptions.NotFoundException;
import com.service.Hotels.exceptions.BadRequestException;
//import javax.validation.constraints.Pattern;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    @Autowired
    private HotelServiceImpl hotelService;
    @Autowired
    private HotelModelAssembler assembler;

    @GetMapping("/{city:[a-zA-Z]+}")
    public CollectionModel<EntityModel<Hotel>> getAll(@PathVariable String city) {
        List<Hotel> hotels = hotelService.getAllHotelsByCity(city);
        if (hotels.isEmpty()) {
            throw new NotFoundException("No se encuentra ningún hotel en la ciudad de " + city);
        }
        List<EntityModel<Hotel>> hotelModels = hotels.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(hotelModels, linkTo(methodOn(HotelController.class).getAll(city)).withSelfRel());
    }

    @GetMapping("/{city}/{id}")
    public EntityModel<Hotel> getHotel(@PathVariable String city, @PathVariable Long id) {
        if(city == null || id == null){
            throw new BadRequestException("El city o el id no deben ser nulos");
        }
        //Obtener la lista de hoteles en la ciudad dada
        List<Hotel> hotels = hotelService.getAllHotelsByCity(city);
        if (hotels.size() <= 0) {
            throw new NotFoundException("No se encuentra ningún hotel en la ciudad de + " + city);
        }

        // Buscar el hotel específico por su ID
        Hotel hotel = hotels.stream()
                .filter(h -> h.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(
                        "No se encuentra ningún hotel en la ciudad de " + city + " con el ID " + id));

        // Convertir el hotel encontrado en un EntityModel
        return assembler.toModel(hotel);
    }

    // Add a new hotel
    @PostMapping("/hotel")
    public ResponseEntity<?> createHotel(@RequestBody Hotel newHotel) {
        EntityModel<Hotel> hotelModel = assembler.toModel(hotelService.addHotel(newHotel));
        return ResponseEntity.created(hotelModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(hotelModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable long id, @RequestBody Hotel newhotel){
        Hotel hotel = hotelService.findHotelById(id);

        hotel.setAddress(newhotel.getAddress() == null ? hotel.getAddress() : newhotel.getAddress());
        hotel.setCity(newhotel.getCity() == null ? hotel.getCity() : newhotel.getCity());
        hotel.setDescription(newhotel.getDescription() == null ? hotel.getDescription() : newhotel.getDescription());
        hotel.setEmail(newhotel.getEmail() == null ? hotel.getEmail() : newhotel.getEmail());
        hotel.setPhone(newhotel.getPhone() == null ? hotel.getPhone() : newhotel.getPhone());
        hotel.setFotos(newhotel.getFotos() == null ? hotel.getFotos() : newhotel.getFotos());
        hotel.setName(newhotel.getName());
        
        Hotel hotelAdded = hotelService.addHotel(hotel);
        return ResponseEntity.ok(hotelAdded);
    }
}
