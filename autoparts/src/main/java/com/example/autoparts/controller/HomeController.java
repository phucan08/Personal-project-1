package com.example.autoparts.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpSession session) {
        Object currentUser = session.getAttribute("currentUser");
        Object role = session.getAttribute("role");

        if (currentUser == null) {
            return "redirect:/login";
        }

        if ("ADMIN".equals(role)) {
            return "redirect:/admin/dashboard";
        }

        return "redirect:/products";
    }
}