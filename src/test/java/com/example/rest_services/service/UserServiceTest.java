package com.example.rest_services.service;

import com.example.rest_services.model.User;
import com.example.rest_services.repository.UserRepository;
import com.example.rest_services.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User user;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setCourseBoughtAt(new Date());
        user.setCourseId(101);
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        assertEquals(1, userService.getAllUsers().size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testCreateUser() {
        userService.createUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testFindUserByUsername() {
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        User foundUser = userService.findUserByUsername("testUser");

        assertNotNull(foundUser);
        assertEquals("testUser", foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testIsAdminTrue() {
        assertTrue(userService.isAdmin("admin"));
    }

    @Test
    void testIsAdminFalse() {
        assertFalse(userService.isAdmin("user"));
    }

    @Test
    void testSaveUser() {
        userService.saveUser(user);
        verify(userRepository, times(1)).save(user);
    }
}
