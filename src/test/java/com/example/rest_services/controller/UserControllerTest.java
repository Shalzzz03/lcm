package com.example.rest_services.controller;

import com.example.rest_services.controller.UserController;
import com.example.rest_services.model.Course;
import com.example.rest_services.model.User;
import com.example.rest_services.service.CourseService;
import com.example.rest_services.service.UserService;
import com.example.rest_services.util.PasswordUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private UserController userController;

    private User user;
    private Course course;

    @BeforeEach
    void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("classpath:/templates/");
        viewResolver.setSuffix(".html");
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword(PasswordUtils.hashPassword("password123"));
        user.setEmail("test@example.com");

        course = new Course();
        course.setCourse_id(1);
        course.setCourse_name("Java Basics");
        course.setCourse_desc("Learn Java from scratch");
    }

    @Test
    void testShowSignupForm() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void testSignupUser() throws Exception {
        mockMvc.perform(post("/signup")
                        .param("username", "testUser")
                        .param("password", "password123")
                        .param("email", "test@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void testLoginUser_Success() throws Exception {
        when(userService.findUserByUsername("testUser")).thenReturn(user);
        when(PasswordUtils.checkPassword("password123", user.getPassword())).thenReturn(true);

        mockMvc.perform(post("/login")
                        .param("username", "testUser")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"));
    }

    @Test
    void testLoginUser_Failure() throws Exception {
        when(userService.findUserByUsername("testUser")).thenReturn(null);

        mockMvc.perform(post("/login")
                        .param("username", "testUser")
                        .param("password", "wrongPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    void testViewCourses_LoggedIn() throws Exception {
        when(courseService.getAllCourses()).thenReturn(Arrays.asList(course));

        mockMvc.perform(get("/courses").sessionAttr("username", "testUser"))
                .andExpect(status().isOk())
                .andExpect(view().name("hello"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attribute("username", "testUser"));
    }

    @Test
    void testBuyCourse_Success() throws Exception {
        when(userService.findUserByUsername("testUser")).thenReturn(user);

        mockMvc.perform(post("/buy")
                        .param("courseId", "1")
                        .param("username", "testUser"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"));

        verify(userService, times(1)).saveUser(user);
    }
}
