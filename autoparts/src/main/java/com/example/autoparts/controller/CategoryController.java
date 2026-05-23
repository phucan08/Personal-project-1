package com.example.autoparts.controller;

import com.example.autoparts.entity.Category;
import com.example.autoparts.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String showCategoryList(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "category/category-list";
    }

    @GetMapping("/add")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("pageTitle", "Add New Category");
        return "category/category-form";
    }

    @PostMapping("/save")
    public String saveCategory(@ModelAttribute("category") Category category,
                               RedirectAttributes redirectAttributes) {
        try {
            categoryService.saveCategory(category);
            redirectAttributes.addFlashAttribute("message", "Category saved successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", getFriendlyErrorMessage(e.getMessage()));
            return "redirect:/categories/add";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot save category.");
            return "redirect:/categories/add";
        }

        return "redirect:/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id,
                                 RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("message", "Category deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete category. It may be used by products.");
        }

        return "redirect:/categories";
    }

    private String getFriendlyErrorMessage(String errorCode) {
        return switch (errorCode) {
            case "DUPLICATE_CATEGORY_NAME" -> "Category name already exists.";
            case "CATEGORY_NAME_REQUIRED" -> "Category name is required.";
            case "CATEGORY_NAME_TOO_LONG" -> "Category name must be less than 100 characters.";
            default -> "Invalid category information.";
        };
    }
}