package com.service.Hotels.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerErrorException;

import com.service.Hotels.assemblers.RoomModelAssembler;
import com.service.Hotels.controllers.RoomController;
import com.service.Hotels.dto.RoomDto;
import com.service.Hotels.exceptions.BadRequestException;
import com.service.Hotels.exceptions.ConflictException;
import com.service.Hotels.exceptions.NotFoundException;
import com.service.Hotels.models.Room;
import com.service.Hotels.repositories.RoomRepository;

@Service
public class RoomService {

    @Autowired
    private RoomRepository repository;

    @Autowired
    private RoomModelAssembler assembler;

    public List<Room> getAllByHotelId(Long id) {
        if (id == null) {
            throw new BadRequestException("The ID cannot be null");
        }
        List<Room> entities = repository.findAllRoomsByHotelId(id);
        if (entities.isEmpty()) {
            throw new NotFoundException("No data found");
        }
        return entities;
    }

    public CollectionModel<EntityModel<Room>> getAll(Long id) {
        // if (id == null) {
        //     throw new BadRequestException("The ID cannot be null");
        // }
        List<Room> rooms = this.getAllByHotelId(id);
        // if (rooms.isEmpty()) {
        //     throw new NotFoundException("Not room found!");
        // }
        List<EntityModel<Room>> roomsModels = rooms.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(roomsModels, linkTo(methodOn(RoomController.class).getAll(id)).withSelfRel());
    }

    public Room add(Room newRoom) {
        if (newRoom == null) {
            throw new BadRequestException("The room to add is null");
        }
        try {
            return repository.save(newRoom);
        } catch (DataIntegrityViolationException e) {
            // si hay violacion en la clave única o intentadno insertar un intedad ya
            throw new ConflictException("Error adding entity: Data integrity violation");
        } catch (Exception e) {
            // Si ocurre cualquier otra excepción no esperada
            throw new ServerErrorException("Error adding entity: " + e.getMessage(), e);
        }
    }

    public Room update(Room newRoom) {
        if (newRoom == null) {
            throw new BadRequestException("The room to add is null");
        }
        try {
            Room entityUpdated = repository.save(newRoom);
            return entityUpdated;
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Error updating Room: Data integrity violation");
        } catch (Exception e) {
            throw new BadRequestException("Error updating Room ");
        }
    }

    public void remove(Long id) {
        if (id == null) {
            throw new BadRequestException("Ehe ID cannot be null");
        }
        if (repository.existsById(id)) {
            try {
                repository.deleteById(id);
            } catch (IllegalArgumentException ex) {
                throw new ConflictException("Error moving Room: Data integrity violation");
            }
        }
    }

    public Room updateRoom(Long id, RoomDto roomDto) {
        Room room = this.findById(id);
        if (room == null) {
            throw new NotFoundException("Room with id: " + id + " was not found");
        }
        room.setAvailable(roomDto.getAvailable() == null ? room.getAvailable() : roomDto.getAvailable());
        room.setDescription(roomDto.getDescription() == null ? room.getDescription() : roomDto.getDescription());
        room.setPrice(roomDto.getPrice() == null ? room.getPrice() : roomDto.getPrice());
        repository.save(room);
        return room;
    }

    private Room findById(Long id) {
        return repository.findById(id).get();
    }

}
