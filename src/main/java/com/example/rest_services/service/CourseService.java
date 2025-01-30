package com.example.rest_services.service;
import com.example.rest_services.model.Course;
import com.example.rest_services.model.User;
import com.example.rest_services.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;


    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course findCourseByCourseId(Integer courseId) {
        Optional<Course> optionalCourse = courseRepository.findByCourseId(courseId);
        return optionalCourse.orElse(null);
    }
    public void saveCourse(Course course) {
        courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }


}

