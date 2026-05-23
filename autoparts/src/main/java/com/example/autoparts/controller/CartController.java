package com.example.autoparts.controller;

import com.example.autoparts.entity.User;
import com.example.autoparts.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String showCart(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");

        model.addAttribute("cartItems", cartService.getCartItems(currentUser));
        model.addAttribute("total", cartService.calculateTotal(currentUser));

        return "cart/cart";
    }

    @GetMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("currentUser");

        try {
            cartService.addToCart(currentUser, productId);
            redirectAttributes.addFlashAttribute("message", "Product added to cart!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", getFriendlyErrorMessage(e.getMessage()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot add product to cart.");
        }

        return "redirect:/products";
    }

    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long cartItemId,
                                 @RequestParam Integer quantity,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("currentUser");

        try {
            cartService.updateQuantity(currentUser, cartItemId, quantity);
            redirectAttributes.addFlashAttribute("message", "Cart updated successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", getFriendlyErrorMessage(e.getMessage()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot update cart.");
        }

        return "redirect:/cart";
    }

    @GetMapping("/remove/{cartItemId}")
    public String removeItem(@PathVariable Long cartItemId,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("currentUser");

        try {
            cartService.removeItem(currentUser, cartItemId);
            redirectAttributes.addFlashAttribute("message", "Item removed from cart!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", getFriendlyErrorMessage(e.getMessage()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot remove item.");
        }

        return "redirect:/cart";
    }

    private String getFriendlyErrorMessage(String errorCode) {
        return switch (errorCode) {
            case "PRODUCT_NOT_FOUND" -> "Product not found.";
            case "PRODUCT_OUT_OF_STOCK" -> "This product is out of stock.";
            case "NOT_ENOUGH_STOCK" -> "Not enough stock available.";
            case "CART_ITEM_NOT_FOUND" -> "Cart item not found.";
            case "INVALID_QUANTITY" -> "Quantity must be greater than 0.";
            case "ACCESS_DENIED" -> "You cannot modify this cart item.";
            default -> "Invalid cart action.";
        };
    }
}
