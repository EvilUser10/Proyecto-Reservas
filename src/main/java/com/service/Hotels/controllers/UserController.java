package com.service.Hotels.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.service.Hotels.repositories.UserRepository;
import com.service.Hotels.services.UserServiceImpl;

import java.util.List;

import com.service.Hotels.dto.UserDto;
import com.service.Hotels.exceptions.BadRequestException;
import com.service.Hotels.exceptions.ForbiddenException;
import com.service.Hotels.exceptions.NotFoundException;
import com.service.Hotels.models.User;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping()
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/profile/{userId}")
    public UserDto getUserProfile(@PathVariable Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.getUserById(userId);
            return new UserDto(user);
        } else {
           throw new ForbiddenException("Access denied");
        }
    }

    @GetMapping("/user/{userName}")
    public UserDto findUser(@PathVariable("userName") String userName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.getUserByUserName(userName);
            return new UserDto(user);
        } else {
           throw new ForbiddenException("Access denied");
        }
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
        if (id == null) {
            throw new BadRequestException("The Id cannot be null.");
        }

        User userFind = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id: " + id + "not found"));

        userFind.setUsername(user.getUsername());
        userFind.setEmail(user.getEmail());
        userFind.setPassword(user.getPassword());;

        try {
            userRepository.save(userFind);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            throw (new BadRequestException("El usuario no se ha podido modificar."));
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        if (id == null) {
            throw new BadRequestException("The Id cannot be null.");
        }
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
