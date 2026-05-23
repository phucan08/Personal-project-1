package com.example.autoparts.repository;

import com.example.autoparts.entity.Product;
import com.example.autoparts.entity.Review;
import com.example.autoparts.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductOrderByCreatedAtDesc(Product product);

    Optional<Review> findByUserAndProduct(User user, Product product);
}
