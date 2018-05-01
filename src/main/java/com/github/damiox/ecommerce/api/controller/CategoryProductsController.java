package com.github.damiox.ecommerce.api.controller;

import com.github.damiox.ecommerce.api.assembler.ProductResourceAssembler;
import com.github.damiox.ecommerce.entity.Category;
import com.github.damiox.ecommerce.entity.Product;
import com.github.damiox.ecommerce.exception.NotFoundException;
import com.github.damiox.ecommerce.service.CategoryService;
import com.github.damiox.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * API Endpoint for categories and products association
 *
 * @author dnardelli
 */
@RestController
@RequestMapping(path = "/categories/{categoryid}/products")
public class CategoryProductsController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductResourceAssembler productResourceAssembler;
    @Autowired
    private PagedResourcesAssembler<Product> pagedResourcesAssembler;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> retrieveAllProducts(@PathVariable Long categoryid, Pageable pageable) {
        // Getting the requiring category; or throwing exception if not found
        final Category category = categoryService.getCategoryById(categoryid)
            .orElseThrow(() -> new NotFoundException("category"));

        // Getting all products in application...
        final Page<Product> products = productService.getAllProducts(category, pageable);

        return ResponseEntity.ok(pagedResourcesAssembler.toResource(products, productResourceAssembler));
    }

    @RequestMapping(path = "/{productid}", method = RequestMethod.POST)
    public ResponseEntity<?> addProduct(@PathVariable Long categoryid, @PathVariable Long productid) {
        // Getting the requiring category; or throwing exception if not found
        final Category category = categoryService.getCategoryById(categoryid)
            .orElseThrow(() -> new NotFoundException("category"));

        // Getting the requiring product; or throwing exception if not found
        final Product product = productService.getProductById(productid)
            .orElseThrow(() -> new NotFoundException("product"));

        // Validating if association does not exist...
        if (productService.hasCategory(product, category)) {
            throw new IllegalArgumentException("product " + product.getId() + " already contains category " + category.getId());
        }

        // Associating product with category...
        productService.addCategory(product, category);

        return ResponseEntity.status(HttpStatus.CREATED).body(productResourceAssembler.toResource(product));
    }

    @RequestMapping(path = "/{productid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeProduct(@PathVariable Long categoryid, @PathVariable Long productid) {
        // Getting the requiring category; or throwing exception if not found
        final Category category = categoryService.getCategoryById(categoryid)
            .orElseThrow(() -> new NotFoundException("category"));

        // Getting the requiring product; or throwing exception if not found
        final Product product = productService.getProductById(productid)
            .orElseThrow(() -> new NotFoundException("product"));

        // Validating if association does not exist...
        if (!productService.hasCategory(product, category)) {
            throw new IllegalArgumentException("product " + product.getId() + " does not contain category " + category.getId());
        }

        // Dis-associating product with category...
        productService.removeCategory(product, category);

        return ResponseEntity.noContent().build();
    }

}
