package com.example.backend.user;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserModel> getUsers() {
        return userRepository.findAll();
    }

    public void addNewUser(UserModel user) {
        if (!userRepository.existsByEmail(user.getEmail())) {
            userRepository.save(user);
        }
    }

    @Transactional
    public void updateUser(Long id, UserModel user) {
        if (userRepository.existsById(id)) {
            user.setId(id);
            userRepository.save(user);
        }
    }
}