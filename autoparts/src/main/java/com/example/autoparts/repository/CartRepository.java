package com.example.autoparts.repository;

import com.example.autoparts.entity.Cart;
import com.example.autoparts.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);
}
