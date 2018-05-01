package com.github.damiox.ecommerce.dao;

import com.github.damiox.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for {@link Category} entity
 *
 * @author dnardelli
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Recursive SQL query to fetch the number of Categories associated with a given category
    // For more information, please visit:
    // https://en.wikipedia.org/wiki/Hierarchical_and_recursive_queries_in_SQL#Common_table_expression
    String GET_RECURSIVELY_ALL_SUBCATEGORIES_SQL = "WITH SUBCATEGORY(ID) AS (select c.ID from app_category c where c.id = ?1 union all select c.ID from SUBCATEGORY inner join app_category c on SUBCATEGORY.ID = c.parentid) select ID from SUBCATEGORY";

}
