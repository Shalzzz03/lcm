package com.example.rest_services.service;

import com.example.rest_services.model.User;
import com.example.rest_services.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean isAdmin(String username) {
        return "admin".equals(username);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }



}