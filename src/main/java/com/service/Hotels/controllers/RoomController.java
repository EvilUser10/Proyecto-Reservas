package com.service.Hotels.controllers;


import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.service.Hotels.services.RoomService;
import com.service.Hotels.assemblers.RoomModelAssembler;
import com.service.Hotels.dto.RoomDto;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService service;
    @Autowired
    private RoomModelAssembler assembler;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/hotel/{id}")
    public ResponseEntity<?> getAll(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAll(id));
    }

    @GetMapping("hotel/{id}/availables")
    public ResponseEntity<?> getAvailables(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAvailableRooms(id));
    }

    // Add a new hotel
    @PostMapping("/room")
    public ResponseEntity<?> addRoom(@RequestBody Room newRoom) {
        EntityModel<Room> roomModel = assembler.toModel(service.add(newRoom));
        return ResponseEntity.created(roomModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(roomModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable long id, @RequestBody RoomDto roomDto) {
        Room room = service.updateRoom(id, roomDto);
        return ResponseEntity.ok(room);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        service.remove(id);
        return ResponseEntity.noContent().build();
    }
}
