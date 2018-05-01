package com.github.damiox.ecommerce.api.controller;

import com.github.damiox.ecommerce.api.assembler.CategoryResourceAssembler;
import com.github.damiox.ecommerce.entity.Category;
import com.github.damiox.ecommerce.exception.NotFoundException;
import com.github.damiox.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * API Endpoint for categories and subcategories association
 * Write operations require ADMIN permission.
 *
 * @author dnardelli
 */
@RestController
@RequestMapping(path = "/categories/{parentid}/subcategories")
public class CategorySubcategoriesController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryResourceAssembler categoryResourceAssembler;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> retrieveAllSubcategories(@PathVariable Long parentid) {
        // Getting the requiring category; or throwing exception if not found
        final Category parent = categoryService.getCategoryById(parentid)
            .orElseThrow(() -> new NotFoundException("parent category"));

        // Getting all categories in application...
        final Set<Category> subcategories = parent.getChildCategories();

        return ResponseEntity.ok(categoryResourceAssembler.toResources(subcategories));
    }

    @RequestMapping(path = "/{childid}", method = RequestMethod.POST)
    public ResponseEntity<?> addSubcategory(@PathVariable Long parentid, @PathVariable Long childid) {
        // Getting the requiring category; or throwing exception if not found
        final Category parent = categoryService.getCategoryById(parentid)
            .orElseThrow(() -> new NotFoundException("parent category"));

        // Getting the requiring category; or throwing exception if not found
        final Category child = categoryService.getCategoryById(childid)
            .orElseThrow(() -> new NotFoundException("child category"));

        // Validating if association does not exist...
        if (categoryService.isChildCategory(child, parent)) {
            throw new IllegalArgumentException("category " + parent.getId() + " already contains subcategory " + child.getId());
        }

        // Associating parent with subcategory...
        categoryService.addChildCategory(child, parent);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResourceAssembler.toResource(parent));
    }

    @RequestMapping(path = "/{childid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeSubcategory(@PathVariable Long parentid, @PathVariable Long childid) {
        // Getting the requiring category; or throwing exception if not found
        final Category parent = categoryService.getCategoryById(parentid)
            .orElseThrow(() -> new NotFoundException("parent category"));

        // Getting the requiring category; or throwing exception if not found
        final Category child = categoryService.getCategoryById(childid)
            .orElseThrow(() -> new NotFoundException("child category"));

        // Validating if association exists...
        if (!categoryService.isChildCategory(child, parent)) {
            throw new IllegalArgumentException("category " + parent.getId() + " does not contain subcategory " + child.getId());
        }

        // Dis-associating parent with subcategory...
        categoryService.removeChildCategory(child, parent);

        return ResponseEntity.noContent().build();
    }

}
