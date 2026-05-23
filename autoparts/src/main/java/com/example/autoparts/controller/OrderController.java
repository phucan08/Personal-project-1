package com.example.autoparts.controller;

import com.example.autoparts.entity.Order;
import com.example.autoparts.entity.Payment;
import com.example.autoparts.entity.User;
import com.example.autoparts.service.CartService;
import com.example.autoparts.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    public OrderController(OrderService orderService,
                           CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping("/checkout")
    public String showCheckoutPage(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("currentUser");

        if (cartService.getCartItems(currentUser).isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Your cart is empty.");
            return "redirect:/cart";
        }

        model.addAttribute("cartItems", cartService.getCartItems(currentUser));
        model.addAttribute("total", cartService.calculateTotal(currentUser));

        model.addAttribute("cashMethod", Payment.METHOD_CASH);
        model.addAttribute("cardMethod", Payment.METHOD_CARD);
        model.addAttribute("bankMethod", Payment.METHOD_BANK_TRANSFER);

        return "order/checkout";
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam String shippingAddress,
                           @RequestParam String paymentMethod,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("currentUser");

        try {
            Order order = orderService.checkout(currentUser, shippingAddress, paymentMethod);
            redirectAttributes.addFlashAttribute("message", "Order created successfully!");
            return "redirect:/orders/" + order.getId();
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", getFriendlyErrorMessage(e.getMessage()));
            return "redirect:/orders/checkout";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot checkout. Please try again.");
            return "redirect:/orders/checkout";
        }
    }

    @GetMapping
    public String orderHistory(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");

        if (User.ROLE_ADMIN.equals(currentUser.getRole())) {
            model.addAttribute("orders", orderService.getAllOrders());
        } else {
            model.addAttribute("orders", orderService.getOrdersByUser(currentUser));
        }

        return "order/order-history";
    }

    @GetMapping("/{id}")
    public String orderDetail(@PathVariable Long id,
                              HttpSession session,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("currentUser");

        try {
            Order order = orderService.getOrderDetailForUser(currentUser, id);
            model.addAttribute("order", order);
            return "order/order-detail";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", getFriendlyErrorMessage(e.getMessage()));
            return "redirect:/orders";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot view order detail.");
            return "redirect:/orders";
        }
    }

    @PostMapping("/update-status")
    public String updateOrderStatus(@RequestParam Long orderId,
                                    @RequestParam String status,
                                    RedirectAttributes redirectAttributes) {
        try {
            orderService.updateOrderStatus(orderId, status);
            redirectAttributes.addFlashAttribute("message", "Order status updated successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", getFriendlyErrorMessage(e.getMessage()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot update order status.");
        }

        return "redirect:/orders/" + orderId;
    }

    private String getFriendlyErrorMessage(String errorCode) {
        return switch (errorCode) {
            case "ORDER_NOT_FOUND" -> "Order not found.";
            case "ACCESS_DENIED" -> "You cannot view this order.";
            case "CART_EMPTY" -> "Your cart is empty.";
            case "NOT_ENOUGH_STOCK" -> "Some products do not have enough stock.";
            case "ADDRESS_REQUIRED" -> "Shipping address is required.";
            case "PAYMENT_METHOD_REQUIRED" -> "Payment method is required.";
            case "INVALID_PAYMENT_METHOD" -> "Invalid payment method.";
            case "INVALID_ORDER_STATUS" -> "Invalid order status.";
            default -> "Invalid order action.";
        };
    }
}
