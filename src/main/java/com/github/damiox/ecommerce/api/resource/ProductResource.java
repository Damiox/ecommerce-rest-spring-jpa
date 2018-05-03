package com.github.damiox.ecommerce.api.resource;

import org.springframework.hateoas.ResourceSupport;

import java.util.List;

/**
 * Spring HATEOAS-oriented DTO for {@see Product} entity
 *
 * @author dnardelli
 */
public class ProductResource extends ResourceSupport {

    private final String name;
    private final String currency;
    private final double price;
    private final List<CategoryResource> categories;
    private final String owner;

    public ProductResource(String name, String currency, double price, List<CategoryResource> categories, String owner) {
        this.name = name;
        this.currency = currency;
        this.price = price;
        this.categories = categories;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
    }

    public Double getPrice() {
        return price;
    }

    public List<CategoryResource> getCategories() {
        return categories;
    }

    public String getOwner() {
        return owner;
    }

}
