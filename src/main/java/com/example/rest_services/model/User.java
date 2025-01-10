package com.example.rest_services.model;

import jakarta.persistence.*;
import java.util.Date;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true, length = 50)
    private String username;

    @NotBlank(message = "Email is required") // Application-level validation (for user feedback)
    @Email(message = "Invalid email format") // Application-level validation
    @Size(max = 100, message = "Email cannot exceed 100 characters") //Application level validation
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;


//    @NotNull(message = "Purchase date is required")
//    @PastOrPresent(message = "Purchase date cannot be in the future") // Example: Must be in the past or present
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // Format for parsing from forms
    @Column(name = "course_bought_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date courseBoughtAt;

    @Column(name = "course_id") // Map to the database column
    private Integer courseId;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCourseBoughtAt() {
        return courseBoughtAt;
    }

    public void setCourseBoughtAt(Date courseBoughtAt) {
        this.courseBoughtAt = courseBoughtAt;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
}