package com.attendance.management_system.controller;

import com.attendance.management_system.model.User;
import com.attendance.management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // 1. New User Create karne ki API (POST Request)
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // 2. Saare Users ki list dekhne ki API (GET Request)
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    // 3. Login karne ki API (POST Request)
    @PostMapping("/login")
    public String loginUser(@RequestBody User loginDetails) {
        // Pehle check karenge ki is username ka koi user database mein hai ya nahi
        java.util.Optional<User> existingUser = userRepository.findByUsername(loginDetails.getUsername());

        if (existingUser.isPresent()) {
            // Agar user mil gaya, toh password match karenge
            User user = existingUser.get();
            if (user.getPassword().equals(loginDetails.getPassword())) {
                return "Login Successful! Role: " + user.getRole();
            } else {
                return "Invalid Password! Try again.";
            }
        } else {
            return "User not found with username: " + loginDetails.getUsername();
        }
    }
}