package com.github.damiox.ecommerce.api.controller;

import com.github.damiox.ecommerce.api.assembler.CategoryResourceAssembler;
import com.github.damiox.ecommerce.entity.Category;
import com.github.damiox.ecommerce.exception.NotFoundException;
import com.github.damiox.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * API Endpoint for categories
 * Write operations require ADMIN permission.
 *
 * @author dnardelli
 */
@RestController
@RequestMapping(path = "/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryResourceAssembler categoryResourceAssembler;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> retrieveAllCategories() {
        // Getting all categories in application...
        final List<Category> categories = categoryService.getAllCategories();

        return ResponseEntity.ok(categoryResourceAssembler.toResources(categories));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> retrieveCategory(@PathVariable Long id) {
        // Getting the requiring category; or throwing exception if not found
        final Category category = categoryService.getCategoryById(id)
            .orElseThrow(() -> new NotFoundException("category"));

        return ResponseEntity.ok(categoryResourceAssembler.toResource(category));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createCategory(@RequestBody @Valid CategoryDto request) {
        // Creating a new category in the application...
        final Category category = categoryService.createCategory(request.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResourceAssembler.toResource(category));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryDto request) {
        // Getting the requiring category; or throwing exception if not found
        final Category category = categoryService.getCategoryById(id)
            .orElseThrow(() -> new NotFoundException("category"));

        // Updating a category in the application...
        categoryService.updateCategory(category, request.getName());

        return ResponseEntity.ok(categoryResourceAssembler.toResource(category));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        // Getting the requiring category; or throwing exception if not found
        final Category category = categoryService.getCategoryById(id)
            .orElseThrow(() -> new NotFoundException("category"));

        // Deleting category from the application...
        categoryService.deleteCategory(category);

        return ResponseEntity.noContent().build();
    }

    static class CategoryDto {
        @NotNull(message = "name is required")
        @Size(message = "name must be equal to or lower than 100", min = 1, max = 100)
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
