package com.service.Hotels.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import com.service.Hotels.controllers.RoomController;
import com.service.Hotels.models.Room;

@Component
public class RoomModelAssembler implements RepresentationModelAssembler<Room,EntityModel<Room>> {

    @Override
    public EntityModel<Room> toModel(Room room) {
        return EntityModel.of(room,
        linkTo(methodOn(RoomController.class).getRoom(room.getId())).withSelfRel(),
        linkTo(methodOn(RoomController.class).getAll(room.getHotel().getId())).withRel("rooms"));
    }
    
}
