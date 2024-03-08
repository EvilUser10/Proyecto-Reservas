package com.service.Hotels.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import com.service.Hotels.controllers.HotelController;
import com.service.Hotels.controllers.RoomController;
import com.service.Hotels.models.Hotel;

@Component
public class HotelModelAssembler implements RepresentationModelAssembler<Hotel,EntityModel<Hotel>> {

    @Override
    public EntityModel<Hotel> toModel(Hotel hotel) {
        return EntityModel.of(hotel,
        linkTo(methodOn(HotelController.class).getHotel(hotel.getCity(),hotel.getId())).withSelfRel(),
        linkTo(methodOn(HotelController.class).getAll(hotel.getCity())).withRel("hotels"),
        linkTo(methodOn(RoomController.class).getAll(hotel.getId())).withRel("rooms")
        );
    }
    
}
