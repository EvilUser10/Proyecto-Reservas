package com.service.Hotels.controllers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.service.Hotels.models.Hotel;
import com.service.Hotels.services.HotelService;
import com.service.Hotels.assemblers.HotelModelAssembler;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;
    @Autowired
    private HotelModelAssembler assembler;

    @GetMapping()
    public ResponseEntity<CollectionModel<EntityModel<Hotel>>> getAllHotels(
            @RequestParam(defaultValue = "10") Integer max,
            @RequestParam(defaultValue = "random") String orderby,
            @RequestParam(required = false) String city) {

        return ResponseEntity.ok(hotelService.getAllHotels(max, orderby, city));
    }

    @GetMapping("/{city:[a-zA-Z]+}")
    public ResponseEntity<?> getAll(@PathVariable String city) {
        return ResponseEntity.ok(hotelService.getAllHotelsByCity(city));
    }

    @GetMapping("/{city}/{id}")
    public ResponseEntity<Hotel> getHotel(@PathVariable String city, @PathVariable Long id) {
        return ResponseEntity.ok(hotelService.findHotelById(id));
    }

    // Add a new hotel
    @PostMapping("/hotel")
    public ResponseEntity<?> createHotel(@RequestBody Hotel newHotel) {
        EntityModel<Hotel> hotelModel = assembler.toModel(hotelService.addHotel(newHotel));
        return ResponseEntity.created(hotelModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(hotelModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable long id, @RequestBody Hotel newHotel) {
        Hotel hotel = hotelService.updateHotel(id, newHotel);
        return ResponseEntity.ok(hotel);
    }
}
