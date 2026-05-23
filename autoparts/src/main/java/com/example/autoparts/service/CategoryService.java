package com.example.autoparts.service;

import com.example.autoparts.entity.Category;
import com.example.autoparts.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CATEGORY_NOT_FOUND"));
    }

    public void saveCategory(Category category) {
        validateCategory(category);

        if (category.getId() == null) {
            if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
                throw new IllegalArgumentException("DUPLICATE_CATEGORY_NAME");
            }
        }

        categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }

    private void validateCategory(Category category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("CATEGORY_NAME_REQUIRED");
        }

        if (category.getName().length() > 100) {
            throw new IllegalArgumentException("CATEGORY_NAME_TOO_LONG");
        }
    }
}