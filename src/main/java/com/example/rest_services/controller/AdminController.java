package com.example.rest_services.controller;

import com.example.rest_services.model.Course;
import com.example.rest_services.model.User;
import com.example.rest_services.service.CourseService;
import com.example.rest_services.service.UserService;
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
    public String viewUsers(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("username");
        if (username != null && userService.isAdmin(username)) {
            List<User> users = userService.getAllUsers();
            model.addAttribute("users", users);

//            List<Course> courses = courseService.getAllCourses();
//            model.addAttribute("courses", courses);



            return "admin/users";
        } else {
            redirectAttributes.addFlashAttribute("message", "You are not authorized to access this page");
            return "redirect:/login";
        }
    }

    @GetMapping("/course_list")
    public String viewCourses(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("username");
//        if (username != null && userService.isAdmin(username)) {
//            List<User> users = userService.getAllUsers();
//            model.addAttribute("users", users);

            List<Course> courses = courseService.getAllCourses();
            model.addAttribute("courses", courses);
            model.addAttribute("newCourse", new Course());

//            model.addAttribute("newCourse", new Course());

            return "admin/course_list";
//        } else {
//            redirectAttributes.addFlashAttribute("message", "You are not authorized to access this page");
//            return "redirect:/login";
//        }
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