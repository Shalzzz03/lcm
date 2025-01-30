package com.example.rest_services.repository;

import com.example.rest_services.model.Course;
import com.example.rest_services.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCourseId(Integer courseId);
}
