
package com.example.autoparts.controller;

import com.example.autoparts.dto.LoginRequest;
import com.example.autoparts.dto.RegisterRequest;
import com.example.autoparts.entity.User;
import com.example.autoparts.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginRequest") LoginRequest loginRequest,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        try {
            User user = userService.login(loginRequest);

            session.setAttribute("currentUser", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("fullName", user.getFullName());
            session.setAttribute("role", user.getRole());

            redirectAttributes.addFlashAttribute("message", "Login successfully!");

            if (user.isAdmin()) {
                return "redirect:/admin/dashboard";
            }

            return "redirect:/products";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", getFriendlyErrorMessage(e.getMessage()));
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot login. Please try again.");
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("registerRequest") RegisterRequest registerRequest,
                           RedirectAttributes redirectAttributes) {
        try {
            userService.register(registerRequest);
            redirectAttributes.addFlashAttribute("message", "Register successfully! Please login.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", getFriendlyErrorMessage(e.getMessage()));
            return "redirect:/register";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot register. Please check your input.");
            return "redirect:/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session,
                         RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "You have been logged out successfully.");
        return "redirect:/login";
    }

    private String getFriendlyErrorMessage(String errorCode) {
        return switch (errorCode) {
            case "EMAIL_REQUIRED" -> "Email is required.";
            case "INVALID_EMAIL" -> "Email format is invalid.";
            case "PASSWORD_REQUIRED" -> "Password is required.";
            case "PASSWORD_TOO_SHORT" -> "Password must be at least 6 characters.";
            case "PASSWORD_NOT_MATCH" -> "Password and confirm password do not match.";
            case "FIRST_NAME_REQUIRED" -> "First name is required.";
            case "LAST_NAME_REQUIRED" -> "Last name is required.";
            case "EMAIL_ALREADY_EXISTS" -> "Email already exists.";
            case "INVALID_LOGIN" -> "Invalid email or password.";
            default -> "Invalid information.";
        };
    }
}