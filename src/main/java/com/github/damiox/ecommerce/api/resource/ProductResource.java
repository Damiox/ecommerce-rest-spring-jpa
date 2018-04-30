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

    public ProductResource(String name, String currency, double price, List<CategoryResource> categories) {
        this.name = name;
        this.currency = currency;
        this.price = price;
        this.categories = categories;
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

}
