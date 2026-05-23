package com.example.autoparts.controller;

import com.example.autoparts.service.OrderService;
import com.example.autoparts.service.ProductService;
import com.example.autoparts.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    private final ProductService productService;
    private final UserService userService;
    private final OrderService orderService;

    public AdminController(ProductService productService,
                           UserService userService,
                           OrderService orderService) {
        this.productService = productService;
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalProducts", productService.getAllProducts().size());
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        model.addAttribute("totalOrders", orderService.getAllOrders().size());

        return "admin/dashboard";
    }

    @GetMapping("/admin/manage-users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/manage-users";
    }

    @GetMapping("/admin/manage-products")
    public String manageProducts(Model model) {
        return "redirect:/products";
    }

    @GetMapping("/admin/manage-orders")
    public String manageOrders() {
        return "redirect:/orders";
    }
}