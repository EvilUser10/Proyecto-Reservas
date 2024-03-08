package com.service.Hotels.controllers;

import java.util.List;
import java.util.stream.Collectors;


import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.Hotels.models.Room;
import com.service.Hotels.services.RoomServiceImp;
import com.service.Hotels.exceptions.NotFoundException;
import com.service.Hotels.assemblers.RoomModelAssembler;
import com.service.Hotels.exceptions.BadRequestException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomServiceImp service;
    @Autowired
    private RoomModelAssembler assembler;

    @GetMapping("/hotel/{id}")
    public CollectionModel<EntityModel<Room>> getAll(@PathVariable Long id) {
        if (id == null) {
            throw new BadRequestException("The ID cannot be null");
        }
        List<Room> rooms = service.getAllByHotelId(id);
        if (rooms.isEmpty()) {
            throw new NotFoundException("Not room found!");
        }
        List<EntityModel<Room>> roomsModels = rooms.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(roomsModels, linkTo(methodOn(RoomController.class).getAll(id)).withSelfRel());
    }

    // @GetMapping("/{id}")
    // public EntityModel<Room> getRoom(@PathVariable Long id) {
    //     if(id == null){
    //         throw new BadRequestException("The ID cannot be null");
    //     }
    //     //Obtener la lista de habitaciones en la ciudad dada
    //     Room room = service.findById(id);
    //     return assembler.toModel(room);
    // }

    // Add a new hotel
    @PostMapping("/room")
    public ResponseEntity<?> addRoom(@RequestBody Room newRoom) {
        EntityModel<Room> roomModel = assembler.toModel(service.add(newRoom));
        return ResponseEntity.created(roomModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(roomModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable long id, @RequestBody Room newRoom){
        Room room = service.findById(id);
        room.setAvailable(newRoom.getAvailable() == null ? room.getAvailable() : newRoom.getAvailable());
        room.setDescription(newRoom.getDescription() == null ? room.getDescription() : newRoom.getDescription());
        room.setPrice(newRoom.getPrice() == null ? room.getPrice() : newRoom.getPrice());
        
        Room roomAdded = service.add(room);
        return ResponseEntity.ok(roomAdded);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        service.remove(id);
        return ResponseEntity.noContent().build();
    }
}
