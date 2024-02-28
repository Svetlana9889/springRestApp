package com.example.springresttask.controller;

import com.example.springresttask.model.User;
import com.example.springresttask.repositories.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserRestController {
    private final UserRepository userRepository;

    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/api/user")
    public User showUser(Principal principal) {
        return userRepository.findByUsername(principal.getName());
    }
}
