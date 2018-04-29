package com.github.damiox.ecommerce.service;

import com.github.damiox.ecommerce.dao.ProductRepository;
import com.github.damiox.ecommerce.entity.Category;
import com.github.damiox.ecommerce.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import javax.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Page<Product> getAllProducts(Category category, Pageable page) {
        return productRepository.findByCategories(category, page);
    }

    @Transactional
    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    @Override
    public Product createProduct(String name, String currency, double price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        // TODO: convert currency

        return productRepository.save(product);
    }

    @Transactional
    @Override
    public void updateProduct(Product product, String name, String currency, double price) {
        // TODO: convert currency

        product.setName(name);
        product.setPrice(price);
        productRepository.save(product);
    }

    @Transactional
    @Override
    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

    @Transactional
    @Override
    public boolean hasCategory(Product product, Category category) {
        return product.getCategories().contains(category);
    }

    @Transactional
    @Override
    public void addCategory(Product product, Category category) {
        product.getCategories().add(category);
        productRepository.save(product);
    }

    @Transactional
    @Override
    public void removeCategory(Product product, Category category) {
        product.getCategories().remove(category);
        productRepository.save(product);
    }

}
