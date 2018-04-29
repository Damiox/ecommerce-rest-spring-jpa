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
            .orElseThrow(NotFoundException::new);

        // Getting all categories in application...
        final Set<Category> subcategories = parent.getSubcategories();

        return ResponseEntity.ok(categoryResourceAssembler.toResources(subcategories));
    }

    @RequestMapping(path = "/{subcategoryid}", method = RequestMethod.POST)
    public ResponseEntity<?> addSubcategory(@PathVariable Long parentid, @PathVariable Long subcategoryid) {
        // Getting the requiring category; or throwing exception if not found
        final Category parent = categoryService.getCategoryById(parentid)
            .orElseThrow(NotFoundException::new);

        // Getting the requiring category; or throwing exception if not found
        final Category subcategory = categoryService.getCategoryById(subcategoryid)
            .orElseThrow(NotFoundException::new);

        // Validating if association does not exist...
        if (categoryService.hasSubcategory(subcategory, parent)) {
            throw new IllegalArgumentException("category " + parent.getId() + " already contains subcategory " + subcategory.getId());
        }

        // Associating parent with subcategory...
        categoryService.addSubcategory(subcategory, parent);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResourceAssembler.toResource(parent));
    }

    @RequestMapping(path = "/{subcategoryid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeSubcategory(@PathVariable Long parentid, @PathVariable Long subcategoryid) {
        // Getting the requiring category; or throwing exception if not found
        final Category parent = categoryService.getCategoryById(parentid)
            .orElseThrow(NotFoundException::new);

        // Getting the requiring category; or throwing exception if not found
        final Category subcategory = categoryService.getCategoryById(subcategoryid)
            .orElseThrow(NotFoundException::new);

        // Validating if association exists...
        if (!categoryService.hasSubcategory(subcategory, parent)) {
            throw new IllegalArgumentException("category " + parent.getId() + " does not contain subcategory " + subcategory.getId());
        }

        // Dis-associating parent with subcategory...
        categoryService.removeSubcategory(subcategory, parent);

        return ResponseEntity.noContent().build();
    }

}
