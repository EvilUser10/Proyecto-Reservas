package com.service.Hotels.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerErrorException;

import com.service.Hotels.exceptions.BadRequestException;
import com.service.Hotels.exceptions.ConflictException;
import com.service.Hotels.exceptions.NotFoundException;
import com.service.Hotels.interfaces.RoomService;
import com.service.Hotels.models.Room;
import com.service.Hotels.repositories.RoomRepository;

@Service
public class RoomServiceImp implements RoomService{

    @Autowired
    private RoomRepository repository;
    @Override
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

    @Override
    public Room findById(Long id) {
        // TODO Solo si necesitamos ver las informaciones del habitacion. en este caso NO.
        throw new UnsupportedOperationException("Unimplemented method 'findById': NO necesitamos ver las informaciones del habitacion");
    }

    @Override
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

    @Override
    public Room update(Room newRoom) {
        if (newRoom == null) {
            throw new BadRequestException("The room to add is null");
        }
        try {
            Room entityUpdated = repository.save(newRoom);
            return entityUpdated;
        } 
        catch(DataIntegrityViolationException e) {
            throw new ConflictException("Error updating Room: Data integrity violation");
        }
        catch (Exception e) {
            throw new BadRequestException("Error updating Room ");
        }
    }

    @Override
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

    
}
