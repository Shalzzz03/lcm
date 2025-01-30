package com.example.rest_services.service;

import com.example.rest_services.model.Course;
import com.example.rest_services.model.User;
import com.example.rest_services.repository.CourseRepository;
import com.example.rest_services.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

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

    public void enrollUserInCourse(Long userId, Integer courseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(Long.valueOf(courseId))
                .orElseThrow(() -> new RuntimeException("Course not found"));

        user.getCourses().add(course);
        userRepository.save(user);
    }

    public void removeUserFromCourse(Long userId, Integer courseId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(Long.valueOf(courseId)).orElseThrow(() -> new RuntimeException("Course not found"));

        user.getCourses().remove(course);

        course.getUsers().remove(user);

        userRepository.save(user);
        courseRepository.save(course);
    }




}