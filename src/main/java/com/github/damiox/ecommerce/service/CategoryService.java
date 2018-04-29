package com.github.damiox.ecommerce.service;

import com.github.damiox.ecommerce.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> getAllCategories();

    Optional<Category> getCategoryById(Long id);

    Category createCategory(String name);

    void updateCategory(Category category, String name);

    void deleteCategory(Category category);

    boolean hasSubcategory(Category category, Category parent);

    void addSubcategory(Category category, Category parent);

    void removeSubcategory(Category category, Category parent);

}
