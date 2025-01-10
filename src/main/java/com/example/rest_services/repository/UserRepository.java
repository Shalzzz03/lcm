package com.example.rest_services.repository; // Create a 'repository' package

import com.example.rest_services.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // You can add custom queries here if needed
    // Example: findByEmail(String email);
    User findByUsername(String username);
}