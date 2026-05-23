package com.example.autoparts.controller;

import com.example.autoparts.entity.Product;
import com.example.autoparts.entity.User;
import com.example.autoparts.service.ProductService;
import com.example.autoparts.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ProductService productService;

    public ReviewController(ReviewService reviewService,
                            ProductService productService) {
        this.reviewService = reviewService;
        this.productService = productService;
    }

    @GetMapping("/add/{productId}")
    public String showReviewForm(@PathVariable Long productId,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.getProductById(productId);
            model.addAttribute("product", product);
            return "review/review-form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Product not found.");
            return "redirect:/products";
        }
    }

    @PostMapping("/save")
    public String saveReview(@RequestParam Long productId,
                             @RequestParam Integer rating,
                             @RequestParam String comment,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("currentUser");

        try {
            reviewService.saveReview(currentUser, productId, rating, comment);
            redirectAttributes.addFlashAttribute("message", "Review submitted successfully!");
            return "redirect:/products/" + productId;
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", getFriendlyErrorMessage(e.getMessage()));
            return "redirect:/reviews/add/" + productId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot submit review.");
            return "redirect:/reviews/add/" + productId;
        }
    }

    private String getFriendlyErrorMessage(String errorCode) {
        return switch (errorCode) {
            case "PRODUCT_NOT_FOUND" -> "Product not found.";
            case "INVALID_RATING" -> "Rating must be between 1 and 5.";
            case "COMMENT_REQUIRED" -> "Comment is required.";
            case "COMMENT_TOO_LONG" -> "Comment must be less than 500 characters.";
            case "NOT_PURCHASED_PRODUCT" -> "You can only review products you have purchased.";
            case "ALREADY_REVIEWED" -> "You have already reviewed this product.";
            default -> "Invalid review information.";
        };
    }
}
