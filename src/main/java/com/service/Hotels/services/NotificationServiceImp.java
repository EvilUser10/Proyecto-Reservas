package com.service.Hotels.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerErrorException;

import com.service.Hotels.exceptions.BadRequestException;
import com.service.Hotels.exceptions.ConflictException;
import com.service.Hotels.exceptions.NotFoundException;
import com.service.Hotels.interfaces.NotificationService;
import com.service.Hotels.models.Notification;
import com.service.Hotels.repositories.NotificacionRepository;

@Service
public class NotificationServiceImp implements NotificationService {

    @Autowired
    private NotificacionRepository repository;

    @Override
    public List<Notification> getAll() {
        List<Notification> entities = repository.findAll();
        if (entities.isEmpty()) {
            throw new NotFoundException("No data found");
        }
        return entities;
    }

    @Override
    public Notification findById(Long id) {
        if (id == null) {
            throw new BadRequestException("The Id cannot be null");
        }
        Notification entity = repository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("No hay ningun hotel con el ID: " + id));
        return entity;
    }

    @Override
    public Notification add(Notification newNotification) {
        if (newNotification == null) {
            throw new BadRequestException("The notification to add is null");
        }
       try {
            return repository.save(newNotification);
        } catch (DataIntegrityViolationException e) {
             // si hay violacion en la clave única o intentadno insertar un intedad ya
            throw new ConflictException("Error adding entity: Data integrity violation");
        } catch (Exception e) {
            // Si ocurre cualquier otra excepción no esperada
            throw new ServerErrorException("Error adding entity: " + e.getMessage(), e);
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
                throw new ConflictException("Error moving notification with ID:" + id +" Data integrity violation");
            }
        }
    }

    @Override
    public List<Notification> getAllByUserId(Long id) {
        if (id == null) {
            throw new BadRequestException("The ID cannot be null");
        }
        List<Notification> entities = repository.findAllNotificationsByUserId(id);
        if (entities.isEmpty()) {
            throw new NotFoundException("None of the notifications were found with userId: " + id );
        }
        return entities;
    }


    @Override
    public List<Notification> getAllByHotelId(Long id) {
        if (id == null) {
            throw new BadRequestException("The ID cannot be null");
        }
        List<Notification> entities = repository.findAllNotificationsByHotelId(id);
        if (entities.isEmpty()) {
            throw new NotFoundException("None of the notifications were found with hotelId: " + id );
        }
        return entities;
    }
    
}
