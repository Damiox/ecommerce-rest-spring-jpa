package com.github.damiox.ecommerce.api.assembler;

import com.github.damiox.ecommerce.api.controller.CategoryController;
import com.github.damiox.ecommerce.api.resource.CategoryResource;
import com.github.damiox.ecommerce.entity.Category;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class CategoryResourceAssembler extends ResourceAssemblerSupport<Category, CategoryResource> {

    public CategoryResourceAssembler() {
        super(CategoryController.class, CategoryResource.class);
    }

    @Override
    protected CategoryResource instantiateResource(Category entity) {
        return new CategoryResource(entity.getName());
    }

    @Override
    public CategoryResource toResource(Category entity) {
        CategoryResource resource = createResourceWithId(entity.getId(), entity);
        if (entity.getParent() != null) {
            resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CategoryController.class).retrieveCategory(entity.getParent().getId())).withRel("parent"));
        }
        if (entity.getSubcategories() != null) {
            //resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CategorySubcategoriesController.class).retrieveAllSubcategories(entity.getParent().getId())).withRel("subcategories"));
        }
        return resource;
    }

}
