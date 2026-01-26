package com.example.backend.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserModel> getUser() {
        return userRepository.findAll();
    }

    public void addNewUser(UserModel user) {
        System.out.println("Adding new user: " + user.getEmail());

        if (!userRepository.existsByEmail(user.getEmail())) {
            userRepository.save(user);
        } else {
            System.out.println("User already exists: " + user.getEmail());
        }
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
