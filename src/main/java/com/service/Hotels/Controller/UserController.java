package com.service.Hotels.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.service.Hotels.Model.User;
import java.util.List;
import java.util.Optional;

import com.service.Hotels.Repository.UserRepository;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/user/{id}")
    public User findUser(@PathVariable("id") Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get(); // Retorna el usuario si está presente
        } else {
            throw new ExceptionInInitializerError("Usuario no encontrado con ID: " + id); // Lanza una excepción si no está                                                              // presente
        }
    }

}
