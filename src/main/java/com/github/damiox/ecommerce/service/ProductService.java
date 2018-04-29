package com.github.damiox.ecommerce.service;

import com.github.damiox.ecommerce.entity.Category;
import com.github.damiox.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {

    Page<Product> getAllProducts(Pageable page);

    Page<Product> getAllProducts(Category category, Pageable page);

    Optional<Product> getProductById(Long id);

    Product createProduct(String name, String currency, double price);

    void updateProduct(Product product, String name, String currency, double price);

    void deleteProduct(Product product);

    boolean hasCategory(Product product, Category category);

    void addCategory(Product product, Category category);

    void removeCategory(Product product, Category category);

}
