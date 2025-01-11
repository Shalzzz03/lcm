package com.example.rest_services.service;

import com.example.rest_services.model.Course;
import com.example.rest_services.repository.CourseRepository;
import com.example.rest_services.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository courseRepository;

    private Course course;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        course = new Course();
        course.setCourse_id(1);
        course.setCourse_name("Java Programming");
        course.setCourse_desc("Comprehensive Java course for beginners.");
    }

    @Test
    void testGetAllCourses() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course));

        assertEquals(1, courseService.getAllCourses().size());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testSaveCourse() {
        courseService.saveCourse(course);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testDeleteCourse() {
        doNothing().when(courseRepository).deleteById(1L);

        courseService.deleteCourse(1L);

        verify(courseRepository, times(1)).deleteById(1L);
    }
}
