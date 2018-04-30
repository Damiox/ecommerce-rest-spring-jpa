package com.github.damiox.ecommerce.dao;

import com.github.damiox.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for {@link User} entity
 *
 * @author dnardelli
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a User through the given username.
     *
     * @param username the username to look for
     * @return the User that was found (if any)
     */
    Optional<User> findByUsername(String username);

}
