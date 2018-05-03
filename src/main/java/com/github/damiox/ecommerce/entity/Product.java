package com.github.damiox.ecommerce.entity;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * A Product is an entity that represents an article for sale.
 * Products are associated with categories either directly or indirectly.
 * For instance, for a given child category B, if it's associated to a
 * parent category A, then the Product is associated with
 * category B (directly) and A (indirectly).
 *
 * @author dnardelli
 */
@Entity
@Table(name = "app_product")
public class Product extends AbstractEntity {

    public static final String CURRENCY = "EUR";

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(name = "app_product_category", joinColumns = @JoinColumn(name = "productid"), inverseJoinColumns = @JoinColumn(name = "categoryid"))
    private Set<Category> categories;

    @Column(name = "price", nullable = false)
    private double price;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false, updatable = false)
    private User user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
