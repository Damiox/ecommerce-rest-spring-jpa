package com.github.damiox.ecommerce.api.resource;

import org.springframework.hateoas.ResourceSupport;

public class CategoryResource extends ResourceSupport {

    private final String name;

    public CategoryResource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
