package com.example.rest_services.controller;

import com.example.rest_services.model.Course;
import com.example.rest_services.model.User;
import com.example.rest_services.service.CourseService;
import com.example.rest_services.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/users")
    public String viewUsers(HttpSession session, Model model, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        String role = (String) session.getAttribute("role");
        if (role != null && role.equals("admin")) {
            try {
                List<User> users = userService.getAllUsers();
                model.addAttribute("users", users);
                return "admin/users";
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Unable to retrieve users");
                return "redirect:/login?error=internal";
            }
        } else {
            return "redirect:/login?error=unauthorized";
        }
    }

        @GetMapping("/course_list")
    public String viewCourses(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String role = (String) session.getAttribute("role");

        if (role != null && role.equals("admin")) {
            List<Course> courses = courseService.getAllCourses();
            model.addAttribute("courses", courses);
            model.addAttribute("newCourse", new Course());
            return "admin/course_list";
        } else {
                return "redirect:/login?error=unauthorized";
        }
    }


    @PostMapping("/users/delete/{id}")
    public String deleteUser(HttpSession session, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("username");
        if (username != null && userService.isAdmin(username)) {
            userService.deleteUser(id);
            return "redirect:/admin/users";
        } else {
            redirectAttributes.addFlashAttribute("message", "You are not authorized to perform this action");
            return "redirect:/login";
        }
    }

    @PostMapping("/courses/add")
    public String addCourse(HttpSession session, @ModelAttribute Course course, RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("username");
        if (username != null && userService.isAdmin(username)) {
            courseService.saveCourse(course);
            return "redirect:/admin/course_list";
        } else {
            redirectAttributes.addFlashAttribute("message", "You are not authorized to perform this action");
            return "redirect:/login";
        }
    }

    @PostMapping("/courses/delete/{id}")
    public String deleteCourse(HttpSession session, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("username");
        if (username != null && userService.isAdmin(username)) {
            courseService.deleteCourse(Long.valueOf(id));
            return "redirect:/admin/course_list";
        } else {
            redirectAttributes.addFlashAttribute("message", "You are not authorized to perform this action");
            return "redirect:/login";
        }
    }
}