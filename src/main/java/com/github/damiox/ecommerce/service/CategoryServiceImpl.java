package com.github.damiox.ecommerce.service;

import com.github.damiox.ecommerce.dao.CategoryRepository;
import com.github.damiox.ecommerce.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional
    @Override
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    @Override
    public Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);

        return categoryRepository.save(category);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    @Override
    public void updateCategory(Category category, String name) {
        category.setName(name);
        categoryRepository.save(category);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    @Override
    public void deleteCategory(Category category) {
        categoryRepository.delete(category);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional
    @Override
    public boolean hasSubcategory(Category category, Category parent) {
        // TODO: must check among all parents!!
        return category.getParent().equals(parent);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    @Override
    public void addSubcategory(Category category, Category parent) {
        category.setParent(parent);
        categoryRepository.save(category);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    @Override
    public void removeSubcategory(Category category, Category parent) {
        category.setParent(null);
        categoryRepository.save(category);
    }

}
