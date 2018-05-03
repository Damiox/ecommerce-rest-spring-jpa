package com.github.damiox.ecommerce.service;

import com.github.damiox.ecommerce.entity.Category;
import com.github.damiox.ecommerce.entity.Product;
import com.github.damiox.ecommerce.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service for {@link Product} entity
 *
 * @author dnardelli
 */
public interface ProductService {

    /**
     * Gets all products present in the system.
     * The result is paginated.
     *
     * @param page the page to fetch results from
     * @return the paginated results
     */
    Page<Product> getAllProducts(Pageable page);

    /**
     * Gets all products that are associated with the given category.
     * The association can be either directly or indirectly.
     * Please {@see Product} entity for more details.
     *
     * @param category the category to filter
     * @param page the page to fetch results from
     * @return the paginated results
     */
    Page<Product> getAllProducts(Category category, Pageable page);

    /**
     * Gets a specific product by looking for its id.
     *
     * @param id the id of the product to look for
     * @return the product (if any)
     */
    Optional<Product> getProductById(Long id);

    /**
     * Creates a product.
     * If the currency is not 'EUR' then a Currency Exchange
     * will be performed.
     *
     * @param name the name of the product
     * @param currency the currency of the product
     * @param price the price of the product
     * @return the new product
     */
    Product createProduct(String name, String currency, double price, User user);

    /**
     * Updates an existing product.
     * If the currency is not 'EUR' then a Currency Exchange
     * will be performed.
     *
     * @param product the product to update
     * @param name the new product name
     * @param currency the new product currency
     * @param price the new product price
     */
    void updateProduct(Product product, String name, String currency, double price);

    /**
     * Deletes a product in the system.
     *
     * @param product the product to delete
     */
    void deleteProduct(Product product);

    /**
     * Checks whether the product has a given category.
     *
     * @param product the product to check
     * @param category the category to check
     * @return true if the product is associated to the category
     */
    boolean hasCategory(Product product, Category category);

    /**
     * Adds a category to the product.
     *
     * @param product the product to add the category to
     * @param category the category to add
     */
    void addCategory(Product product, Category category);

    /**
     * Removes a category from the product.
     *
     * @param product the product to remove the category from
     * @param category the category to remove
     */
    void removeCategory(Product product, Category category);

    /**
     * Checks whether or not a given category has products associated.
     * The association can be either directly or indirectly.
     * Please {@see Product} entity for more details.
     *
     * @param category the category to check
     */
    boolean hasProductsAssociated(Category category);

}
