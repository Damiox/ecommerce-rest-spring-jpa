package com.github.damiox.ecommerce.dao;

import com.github.damiox.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA Repository for {@link Category} entity
 *
 * @author dnardelli
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
