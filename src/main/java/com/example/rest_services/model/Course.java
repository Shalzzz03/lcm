package com.example.rest_services.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseId;

    @Column(nullable = false, length = 50)
    private String course_name;

    @Column(nullable = false, length = 50)
    private String course_desc;

    @Column(nullable = false)
    private Integer course_price;

    @ManyToMany(mappedBy = "courses")
    private Set<User> users = new HashSet<>();


    // Getter and Setter methods with correct naming conventions
    public Integer getCourse_id() {
        return courseId;
    }

    public void setCourse_id(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_desc() {
        return course_desc;
    }

    public void setCourse_desc(String course_desc) {
        this.course_desc = course_desc;
    }

    public Integer getCourse_price() {
        return course_price;
    }

    public void setCourse_price(Integer course_price) {
        this.course_price = course_price;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}
