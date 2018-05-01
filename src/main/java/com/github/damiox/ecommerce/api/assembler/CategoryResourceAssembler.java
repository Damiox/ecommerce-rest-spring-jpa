package com.github.damiox.ecommerce.api.assembler;

import com.github.damiox.ecommerce.api.controller.CategoryController;
import com.github.damiox.ecommerce.api.controller.CategoryProductsController;
import com.github.damiox.ecommerce.api.controller.CategorySubcategoriesController;
import com.github.damiox.ecommerce.api.resource.CategoryResource;
import com.github.damiox.ecommerce.entity.Category;
import com.github.damiox.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 * Transform {@link Category} into {@link CategoryResource} DTO
 *
 * @author dnardelli
 */
@Component
public class CategoryResourceAssembler extends ResourceAssemblerSupport<Category, CategoryResource> {

    @Autowired
    private ProductService productService;

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
        if (entity.getChildCategories() != null) {
            resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CategorySubcategoriesController.class).retrieveAllSubcategories(entity.getId())).withRel("subcategories"));
        }
        if (productService.hasProductsAssociated(entity)) {
            resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CategoryProductsController.class).retrieveAllProducts(entity.getId(), null)).withRel("products"));
        }

        return resource;
    }

}
