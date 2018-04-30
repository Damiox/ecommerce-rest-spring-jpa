package com.github.damiox.ecommerce.dao;

import com.github.damiox.ecommerce.entity.Category;
import com.github.damiox.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA Repository for {@link Product} entity
 *
 * @author dnardelli
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Finds the products associated with a given category.
     *
     * @param category the category to look for
     * @param pageable the page to fetch data from
     * @return the paginated products
     */
    Page<Product> findByCategories(Category category, Pageable pageable);

}
