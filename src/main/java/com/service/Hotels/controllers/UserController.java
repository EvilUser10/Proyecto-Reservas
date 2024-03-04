package com.service.Hotels.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    //     Optional<User> optionalUser = userRepository.findById(id);
    //     if (optionalUser.isPresent()) {
    //         User user = optionalUser.get();
    //         return ResponseEntity.ok(user);
    //     } else {
    //         return ResponseEntity.notFound().build();
    //     }
    // }

    @GetMapping("/user/{id}")
    public User findUser(@PathVariable("id") Long id) {
        User optionalUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id: " + id  + "not found "));
        return optionalUser;
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody User newUser) {

        User savedUser = userRepository.save(newUser);

        if (savedUser != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> editUser(@PathVariable("id") Long id, @RequestBody User user) {
        User editedUser = userRepository.save(user);

        if (editedUser != null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(editedUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // Eliminación exitosa
        } else {
            return ResponseEntity.notFound().build(); // Usuario no encontrado
        }
    }
}
