package com.example.backend.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST,
        RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
public class UserController {

    private UserService userService;
    private UserRepository userRepository;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserModel> getUser() {
        return userService.getUsers();
    }

    @PostMapping
    public void addNewUser(@RequestBody UserModel user) {
        userService.addNewUser(user);
    }

    @PutMapping(path = "{id}")
    public void updateUser(@PathVariable("id") Long id, @RequestBody UserModel user) {

        userService.updateUser(id, user);
    }

    @PostMapping("/login")
    public ResponseEntity<UserModel> login(@RequestBody LoginRequest req) {
        UserModel user = userService.login(req.getEmail(), req.getPassword());
        return ResponseEntity.ok(user);
    }

}
