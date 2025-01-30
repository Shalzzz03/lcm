package com.example.rest_services.controller;

import com.example.rest_services.model.Course;
import com.example.rest_services.model.User;
import com.example.rest_services.service.CourseService;
import com.example.rest_services.service.UserService;
import com.example.rest_services.util.PasswordUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupUser(@ModelAttribute("user") User user) {
        String hashedPassword = PasswordUtils.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        String email = user.getEmail();
        String emailRegex = "^[A-Za-z0-9._%+-]+@([A-Za-z0-9-]+\\.)?xecurify\\.com$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        if(matcher.matches()){
            user.setRole("user");
            userService.createUser(user);
        }else {
            return "redirect:/signup?error=invalidEmail";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        return "login";
    }

    @PostMapping("/logout")
    public String showLoginFormAfterLogout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.invalidate();
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password, HttpSession session) {
        User user = userService.findUserByUsername(username);

        if (user != null && PasswordUtils.checkPassword(password, user.getPassword())) {
            session.setAttribute("username", username);
            session.setAttribute("role", user.getRole());

            if ("admin".equals(user.getRole())) {
                return "redirect:/admin/users";
            } else {
                return "redirect:/courses";
            }
        } else {
            return "redirect:/login?error";
        }
    }

    @GetMapping("/hello")
    public String hello(HttpSession session, Model model,HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");
        if (role != null) {
            model.addAttribute("username", username);
            return "hello";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/courses")
    public String viewCourses(HttpSession session, Model model, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");
        if (role != null) {
            List<Course> courses = courseService.getAllCourses();
            model.addAttribute("courses", courses);
            model.addAttribute("username", username);
            return "hello";
        } else {
            redirectAttributes.addFlashAttribute("message", "Please log in to view courses");
            return "redirect:/login";
        }
    }

//    @PostMapping("/buy")
//    public String buyCourse(@RequestParam Integer courseId, @RequestParam String username, RedirectAttributes redirectAttributes, HttpSession session) {
//        User user = userService.findUserByUsername(username);
//
//        if (user != null) {
//            user.setCourseId(courseId);
//            userService.saveUser(user);
//            session.setAttribute("courseId", courseId);
//            redirectAttributes.addFlashAttribute("message", "Course purchased successfully!");
//            return "redirect:/courses";
//        } else {
//            redirectAttributes.addFlashAttribute("message", "User not found");
//            return "redirect:/login?error";
//        }
//    }
        @PostMapping("/buy")
        public String buyCourse(@RequestParam Integer courseId, @RequestParam String username,
                                RedirectAttributes redirectAttributes, HttpSession session) {
            User user = userService.findUserByUsername(username);

            if (user != null) {
                userService.enrollUserInCourse(user.getId(), courseId);
                session.setAttribute("courseId", courseId);
                redirectAttributes.addFlashAttribute("message", "Course purchased successfully!");
                return "redirect:/courses";
            } else {
                redirectAttributes.addFlashAttribute("message", "User not found");
                return "redirect:/login?error";
            }
        }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        String username = (String) session.getAttribute("username");
        Integer courseId = (Integer) session.getAttribute("courseId");
        String role = (String) session.getAttribute("role");
        if (role != null) {
            User user = userService.findUserByUsername(username);
            model.addAttribute("user", user);
            model.addAttribute("username", username);

            if (user.getCourseId() != null) {
                Course course = courseService.findCourseByCourseId(user.getCourseId());
                model.addAttribute("course", course);

                if (course == null) { // Handle the case where the course was not found
                    model.addAttribute("errorMessage", "Course not found."); // Or other appropriate message
                }
            } else {
                model.addAttribute("errorMessage", "No course associated with this user."); // Or other appropriate message
            }
            return "profile";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute User updatedUser, HttpSession session, RedirectAttributes redirectAttributes) {
        String currentUsername = (String) session.getAttribute("username");
        if (currentUsername != null) {
            User user = userService.findUserByUsername(currentUsername);
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            userService.saveUser(user);
            session.setAttribute("username", updatedUser.getUsername());
            redirectAttributes.addFlashAttribute("message", "Profile updated successfully!");
            return "redirect:/profile";
        } else {
            return "redirect:/login";
        }
    }


    @PostMapping("/users/delete/{id}/{course_id}")
    public String deleteUser(HttpSession session, @PathVariable Integer id, @PathVariable Integer course_id, RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            userService.removeUserFromCourse(Long.valueOf(id), course_id);
            return "redirect:/profile";
        } else {
            redirectAttributes.addFlashAttribute("message", "You are not authorized to perform this action");
            return "redirect:/login";
        }
    }

}
