package com.github.damiox.ecommerce.service;

import com.github.damiox.ecommerce.dao.ProductRepository;
import com.github.damiox.ecommerce.entity.Category;
import com.github.damiox.ecommerce.entity.Product;
import com.github.damiox.ecommerce.entity.User;
import com.github.damiox.ecommerce.util.CurrencyExchangeCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;
import javax.transaction.Transactional;

/**
 * Spring-oriented Implementation for {@link ProductService}
 *
 * @author dnardelli
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CurrencyExchangeCommand currencyExchangeCommand;

    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional
    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional
    @Override
    public Page<Product> getAllProducts(Category category, Pageable page) {
        return productRepository.findByAssociatedWithCategory(category.getId(), page);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional
    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional
    @Override
    public Product createProduct(String name, String currency, double price, User user) {
        if (!Product.CURRENCY.equals(currency)) {
            price = currencyExchangeCommand.convert(currency, Product.CURRENCY, price);
        }

        // Round up only 2 decimals...
        price = (double) Math.round(price * 100) / 100;

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setUser(user);

        return productRepository.save(product);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #product.getUser().getId()")
    @Transactional
    @Override
    public void updateProduct(Product product, String name, String currency, double price) {
        if (!Product.CURRENCY.equals(currency)) {
            price = currencyExchangeCommand.convert(currency, Product.CURRENCY, price);
        }

        // Round up only 2 decimals...
        price = (double) Math.round(price * 100) / 100;

        product.setName(name);
        product.setPrice(price);
        productRepository.save(product);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #product.getUser().getId()")
    @Transactional
    @Override
    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional
    @Override
    public boolean hasCategory(Product product, Category category) {
        return product.getCategories().contains(category);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #product.getUser().getId()")
    @Transactional
    @Override
    public void addCategory(Product product, Category category) {
        product.getCategories().add(category);
        productRepository.save(product);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #product.getUser().getId()")
    @Transactional
    @Override
    public void removeCategory(Product product, Category category) {
        product.getCategories().remove(category);
        productRepository.save(product);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional
    @Override
    public boolean hasProductsAssociated(Category category) {
        return productRepository.countByAssociatedWithCategory(category.getId()) > 0;
    }

}
