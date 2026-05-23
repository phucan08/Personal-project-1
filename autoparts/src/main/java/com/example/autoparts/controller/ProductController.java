package com.example.autoparts.controller;

import com.example.autoparts.entity.Category;
import com.example.autoparts.entity.Product;
import com.example.autoparts.service.CategoryService;
import com.example.autoparts.service.ProductService;
import com.example.autoparts.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;

    public ProductController(ProductService productService, CategoryService categoryService, ReviewService reviewService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.reviewService = reviewService;
    }

    @GetMapping
    public String showProductList(@RequestParam(value = "keyword", required = false) String keyword,
                                  Model model) {
        model.addAttribute("products", productService.searchProducts(keyword));
        model.addAttribute("keyword", keyword);
        return "product/product-list";
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("pageTitle", "Add New Product");
        return "product/product-form";
    }

    @GetMapping("/{id}")
    public String showProductDetail(@PathVariable Long id,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.getProductById(id);

            model.addAttribute("product", product);
            model.addAttribute("reviews", reviewService.getReviewsByProduct(id));

            return "product/product-detail";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Product not found.");
            return "redirect:/products";
        }
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product,
                              @RequestParam(value = "categoryId", required = false) Long categoryId,
                              RedirectAttributes redirectAttributes) {
        try {
            if (categoryId != null) {
                Category category = categoryService.getCategoryById(categoryId);
                product.setCategory(category);
            }

            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("message", "Product saved successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", getFriendlyErrorMessage(e.getMessage()));

            if (product.getId() != null) {
                return "redirect:/products/edit/" + product.getId();
            }

            return "redirect:/products/add";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot save product. Please check your input.");

            if (product.getId() != null) {
                return "redirect:/products/edit/" + product.getId();
            }

            return "redirect:/products/add";
        }

        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable Long id,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.getProductById(id);
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("pageTitle", "Edit Product");
            return "product/product-form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Product not found.");
            return "redirect:/products";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("message", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete product.");
        }

        return "redirect:/products";
    }

    private String getFriendlyErrorMessage(String errorCode) {
        return switch (errorCode) {
            case "DUPLICATE_PRODUCT_CODE" -> "Product code already exists.";
            case "PRODUCT_CODE_REQUIRED" -> "Product code is required.";
            case "PRODUCT_CODE_TOO_LONG" -> "Product code must be less than 30 characters.";
            case "PRODUCT_NAME_REQUIRED" -> "Product name is required.";
            case "INVALID_PRICE" -> "Price must be greater than or equal to 0.";
            case "INVALID_QUANTITY" -> "Quantity must be greater than or equal to 0.";
            case "CATEGORY_NOT_FOUND" -> "Selected category does not exist.";
            default -> "Invalid product information.";
        };
    }
}