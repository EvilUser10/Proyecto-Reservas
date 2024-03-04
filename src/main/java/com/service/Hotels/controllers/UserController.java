package com.service.Hotels.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.service.Hotels.repositories.UserRepository;
import java.util.List;

import com.service.Hotels.exceptions.BadRequestException;
import com.service.Hotels.exceptions.NotFoundException;
import com.service.Hotels.models.User;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    // @GetMapping("/user/{id}")
    // public ResponseEntity<?> findUser(@PathVariable("id") Long id) {
    // Optional<User> optionalUser = userRepository.findById(id);
    // if (optionalUser.isPresent()) {
    // User user = optionalUser.get();
    // return ResponseEntity.ok(user);
    // } else {
    // return ResponseEntity.notFound().build();
    // }
    // }

    @GetMapping("/user/{id}")
    public User findUser(@PathVariable("id") Long id) {
        User optionalUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id: " + id + "not found"));
        return optionalUser;
    }

    @PostMapping("/user")
    public User createUser(@RequestBody User newUser) {
        try {
            User savedUser = userRepository.save(newUser);
            return savedUser;
        } catch (Exception e) {
            throw (new BadRequestException("El usuario no se ha podido crear."));
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> editUser(@PathVariable("id") Long id, @RequestBody User user) {

        User userFind = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id: " + id + "not found"));

        userFind.setEmail(user.getEmail());
        userFind.setName(user.getName());
        userFind.setPassword(user.getPassword());
        userFind.setSurname(user.getSurname());

        try {
            userRepository.save(userFind);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            throw (new BadRequestException("El usuario no se ha podido modificar."));
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        if (userRepository.existsById(id)) {
            try {
                userRepository.deleteById(id);

            } catch (Exception e) {
                throw new BadRequestException("El usuario con el id: " + id + " no se ha podido borrar.");
            }
            return ResponseEntity.noContent().build();
        } else {
            throw new NotFoundException("El usuario con el id: " + id + " no se ha encontrado.");
        }
    }
}
