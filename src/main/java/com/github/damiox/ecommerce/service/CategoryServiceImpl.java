package com.github.damiox.ecommerce.service;

import com.github.damiox.ecommerce.dao.CategoryRepository;
import com.github.damiox.ecommerce.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

/**
 * Spring-oriented Implementation for {@link CategoryService}
 *
 * @author dnardelli
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @PreAuthorize("hasRole('ROLE_USER')")
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
    public boolean isChildCategory(Category category, Category parent) {
        return category.getParent().equals(parent);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    @Override
    public void addChildCategory(Category category, Category parent) {
        category.setParent(parent);
        categoryRepository.save(category);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    @Override
    public void removeChildCategory(Category category, Category parent) {
        category.setParent(null);
        categoryRepository.save(category);
    }

}
