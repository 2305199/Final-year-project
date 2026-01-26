package com.example.backend.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private UserService userService;
    private UserRepository userRepository;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserModel> getUser() {
        return userService.getUser();
    }

    @PostMapping
    public void addNewUser(@RequestBody UserModel user) {
        userService.addNewUser(user);
    }

    @Transactional
    public void updateUser(Long id, UserModel user) {
        if (userRepository.existsById(id)) {
            user.setId(id); // make sure the ID is set
            userRepository.save(user);
            System.out.println("Updated user with ID: " + id);
        } else {
            System.out.println("User not found with ID: " + id);
        }
    }

}
