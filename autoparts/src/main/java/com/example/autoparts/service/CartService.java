package com.example.autoparts.service;

import com.example.autoparts.entity.Cart;
import com.example.autoparts.entity.CartItem;
import com.example.autoparts.entity.Product;
import com.example.autoparts.entity.User;
import com.example.autoparts.repository.CartItemRepository;
import com.example.autoparts.repository.CartRepository;
import com.example.autoparts.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public Cart getCartByUser(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(new Cart(user)));
    }

    public List<CartItem> getCartItems(User user) {
        Cart cart = getCartByUser(user);
        return cartItemRepository.findByCart(cart);
    }

    public void addToCart(User user, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("PRODUCT_NOT_FOUND"));

        if (product.getQuantity() == null || product.getQuantity() <= 0) {
            throw new IllegalArgumentException("PRODUCT_OUT_OF_STOCK");
        }

        Cart cart = getCartByUser(user);

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElse(null);

        if (cartItem == null) {
            cartItem = new CartItem(cart, product, 1);
        } else {
            if (cartItem.getQuantity() + 1 > product.getQuantity()) {
                throw new IllegalArgumentException("NOT_ENOUGH_STOCK");
            }

            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }

        cartItemRepository.save(cartItem);
    }

    public void updateQuantity(User user, Long cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("CART_ITEM_NOT_FOUND"));

        if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("ACCESS_DENIED");
        }

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("INVALID_QUANTITY");
        }

        if (quantity > cartItem.getProduct().getQuantity()) {
            throw new IllegalArgumentException("NOT_ENOUGH_STOCK");
        }

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    public void removeItem(User user, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("CART_ITEM_NOT_FOUND"));

        if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("ACCESS_DENIED");
        }

        cartItemRepository.delete(cartItem);
    }

    public BigDecimal calculateTotal(User user) {
        List<CartItem> cartItems = getCartItems(user);

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cartItems) {
            total = total.add(item.getSubtotal());
        }

        return total;
    }

    public void clearCart(User user) {
        Cart cart = getCartByUser(user);
        cartItemRepository.deleteByCart(cart);
    }
}