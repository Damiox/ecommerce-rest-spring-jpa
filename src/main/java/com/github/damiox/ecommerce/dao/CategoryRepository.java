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
    // H2 only support recursive CTE without named parameter, that's why this query cannot have any parameter.
    String GET_RECURSIVELY_ALL_SUBCATEGORIES_SQL = "WITH RECURSIVE ALL_SUBCATEGORIES(ID, PARENTID) AS (select c.id, c.parentid from app_category c where c.parentid is null union all select c.id, c.parentid from ALL_SUBCATEGORIES inner join app_category c on ALL_SUBCATEGORIES.id = c.parentid) select id, parentid from ALL_SUBCATEGORIES";

}
