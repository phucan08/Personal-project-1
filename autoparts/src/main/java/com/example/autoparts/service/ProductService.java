package com.example.autoparts.service;

import com.example.autoparts.entity.Product;
import com.example.autoparts.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllProducts();
        }

        return productRepository.findByNameContainingIgnoreCaseOrProductCodeContainingIgnoreCase(
                keyword,
                keyword
        );
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PRODUCT_NOT_FOUND"));
    }

    public void saveProduct(Product product) {
        validateProduct(product);

        if (product.getId() == null) {
            if (productRepository.existsByProductCode(product.getProductCode())) {
                throw new IllegalArgumentException("DUPLICATE_PRODUCT_CODE");
            }
        } else {
            Product oldProduct = getProductById(product.getId());

            if (!oldProduct.getProductCode().equals(product.getProductCode())
                    && productRepository.existsByProductCode(product.getProductCode())) {
                throw new IllegalArgumentException("DUPLICATE_PRODUCT_CODE");
            }
        }

        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    private void validateProduct(Product product) {
        if (product.getProductCode() == null || product.getProductCode().trim().isEmpty()) {
            throw new IllegalArgumentException("PRODUCT_CODE_REQUIRED");
        }

        if (product.getProductCode().length() > 30) {
            throw new IllegalArgumentException("PRODUCT_CODE_TOO_LONG");
        }

        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("PRODUCT_NAME_REQUIRED");
        }

        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("INVALID_PRICE");
        }

        if (product.getQuantity() == null || product.getQuantity() < 0) {
            throw new IllegalArgumentException("INVALID_QUANTITY");
        }
    }
}