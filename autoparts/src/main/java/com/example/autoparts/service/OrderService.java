package com.example.autoparts.service;

import com.example.autoparts.entity.*;
import com.example.autoparts.repository.OrderRepository;
import com.example.autoparts.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        CartService cartService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByOrderDateDesc();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ORDER_NOT_FOUND"));
    }

    public Order getOrderDetailForUser(User user, Long orderId) {
        Order order = getOrderById(orderId);

        if (!order.getUser().getId().equals(user.getId()) && !User.ROLE_ADMIN.equals(user.getRole())) {
            throw new IllegalArgumentException("ACCESS_DENIED");
        }

        return order;
    }

    @Transactional
    public Order checkout(User user, String shippingAddress, String paymentMethod) {
        validateCheckout(shippingAddress, paymentMethod);

        List<CartItem> cartItems = cartService.getCartItems(user);

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("CART_EMPTY");
        }

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new IllegalArgumentException("NOT_ENOUGH_STOCK");
            }

            total = total.add(cartItem.getSubtotal());
        }

        Payment payment = new Payment(paymentMethod, Payment.STATUS_PENDING, total);

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(shippingAddress.trim());
        order.setOrderStatus(Order.STATUS_PENDING);
        order.setTotalAmount(total);
        order.setPayment(payment);

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            OrderItem orderItem = new OrderItem(
                    product,
                    cartItem.getQuantity(),
                    product.getPrice()
            );

            order.addOrderItem(orderItem);

            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            product.setAvailable(product.getQuantity() > 0);
            productRepository.save(product);
        }

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(user);

        return savedOrder;
    }

    public void updateOrderStatus(Long orderId, String status) {
        Order order = getOrderById(orderId);

        if (!Order.STATUS_PENDING.equals(status)
                && !Order.STATUS_CONFIRMED.equals(status)
                && !Order.STATUS_CANCELLED.equals(status)) {
            throw new IllegalArgumentException("INVALID_ORDER_STATUS");
        }

        order.setOrderStatus(status);
        orderRepository.save(order);
    }

    private void validateCheckout(String shippingAddress, String paymentMethod) {
        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("ADDRESS_REQUIRED");
        }

        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new IllegalArgumentException("PAYMENT_METHOD_REQUIRED");
        }

        if (!Payment.METHOD_CASH.equals(paymentMethod)
                && !Payment.METHOD_CARD.equals(paymentMethod)
                && !Payment.METHOD_BANK_TRANSFER.equals(paymentMethod)) {
            throw new IllegalArgumentException("INVALID_PAYMENT_METHOD");
        }
    }
}