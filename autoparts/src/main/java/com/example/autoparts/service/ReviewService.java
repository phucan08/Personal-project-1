package com.example.autoparts.service;

import com.example.autoparts.entity.Product;
import com.example.autoparts.entity.Review;
import com.example.autoparts.entity.User;
import com.example.autoparts.repository.ProductRepository;
import com.example.autoparts.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final OrderService orderService;

    public ReviewService(ReviewRepository reviewRepository,
                         ProductRepository productRepository,
                         OrderService orderService) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.orderService = orderService;
    }

    public List<Review> getReviewsByProduct(Long productId) {
        Product product = getProductById(productId);
        return reviewRepository.findByProductOrderByCreatedAtDesc(product);
    }

    public void saveReview(User user, Long productId, Integer rating, String comment) {
        Product product = getProductById(productId);

        validateReview(rating, comment);

        if (!hasUserPurchasedProduct(user, product)) {
            throw new IllegalArgumentException("NOT_PURCHASED_PRODUCT");
        }

        if (reviewRepository.findByUserAndProduct(user, product).isPresent()) {
            throw new IllegalArgumentException("ALREADY_REVIEWED");
        }

        Review review = new Review(user, product, rating, comment.trim());
        reviewRepository.save(review);
    }

    private boolean hasUserPurchasedProduct(User user, Product product) {
        return orderService.getOrdersByUser(user)
                .stream()
                .flatMap(order -> order.getOrderItems().stream())
                .anyMatch(item -> item.getProduct().getId().equals(product.getId()));
    }

    private Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("PRODUCT_NOT_FOUND"));
    }

    private void validateReview(Integer rating, String comment) {
        if (rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("INVALID_RATING");
        }

        if (comment == null || comment.trim().isEmpty()) {
            throw new IllegalArgumentException("COMMENT_REQUIRED");
        }

        if (comment.length() > 500) {
            throw new IllegalArgumentException("COMMENT_TOO_LONG");
        }
    }
}
