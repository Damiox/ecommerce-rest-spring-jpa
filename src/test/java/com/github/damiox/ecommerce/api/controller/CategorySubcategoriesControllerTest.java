package com.github.damiox.ecommerce.api.controller;

import com.github.damiox.ecommerce.api.assembler.CategoryResourceAssembler;
import com.github.damiox.ecommerce.entity.Category;
import com.github.damiox.ecommerce.security.JWTAuthenticationFilter;
import com.github.damiox.ecommerce.service.CategoryService;
import com.github.damiox.ecommerce.service.ProductService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

/**
 * Unit tests for {@link CategorySubcategoriesController}
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = CategorySubcategoriesController.class, secure = false, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JWTAuthenticationFilter.class))
public class CategorySubcategoriesControllerTest {

    private static final String URL = "/categories/{parentid}/subcategories";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CategoryService categoryService;
    @SpyBean
    private CategoryResourceAssembler categoryResourceAssembler;
    @MockBean
    private ProductService productService;

    @Test
    public void testRetrieveAllSubcategories() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("C1");
        Category child1 = new Category();
        child1.setId(2L);
        child1.setName("C2");
        Category child2 = new Category();
        child2.setId(3L);
        child2.setName("C3");
        category.setChildCategories(new HashSet<>(Arrays.asList(child1, child2)));
        child1.setParent(category);
        child2.setParent(category);

        Mockito.when(categoryService.getCategoryById(Mockito.eq(1L))).thenReturn(Optional.of(category));

        mockMvc.perform(MockMvcRequestBuilders.get(URL, "1"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name", Matchers.is("C2")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].links[0].rel", Matchers.is("self")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].links[0].href", Matchers.is("http://localhost/categories/2")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].links[1].rel", Matchers.is("parent")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].links[1].href", Matchers.is("http://localhost/categories/1")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[1].name", Matchers.is("C3")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[1].links[0].rel", Matchers.is("self")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[1].links[0].href", Matchers.is("http://localhost/categories/3")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[1].links[1].rel", Matchers.is("parent")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[1].links[1].href", Matchers.is("http://localhost/categories/1")));
    }

    @Test
    public void testAddSubcategory() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("C1");
        Category child = new Category();
        child.setId(2L);
        child.setName("C2");
        category.setChildCategories(Collections.singleton(child));

        // Testing success scenario
        Mockito.when(categoryService.getCategoryById(Mockito.eq(1L))).thenReturn(Optional.of(category));
        Mockito.when(categoryService.getCategoryById(Mockito.eq(2L))).thenReturn(Optional.of(child));
        Mockito.when(categoryService.isChildCategory(Mockito.eq(child), Mockito.eq(category))).thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders.post(URL + "/{childid}", "1", "2"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("C1")))
            .andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href", Matchers.is("http://localhost/categories/1")))
            .andExpect(MockMvcResultMatchers.jsonPath("$._links.subcategories.href", Matchers.is("http://localhost/categories/1/subcategories")));
        Mockito.verify(categoryService).addChildCategory(Mockito.eq(child), Mockito.eq(category));

        Mockito.reset(categoryService);

        // Testing fail scenario: subcategory is already associated with parent category
        Mockito.when(categoryService.getCategoryById(Mockito.eq(1L))).thenReturn(Optional.of(category));
        Mockito.when(categoryService.getCategoryById(Mockito.eq(2L))).thenReturn(Optional.of(child));
        Mockito.when(categoryService.isChildCategory(Mockito.eq(child), Mockito.eq(category))).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.post(URL + "/{childid}", "1", "2"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
        Mockito.verify(categoryService, Mockito.never()).addChildCategory(Mockito.eq(child), Mockito.eq(category));
    }

    @Test
    public void testRemoveSubcategory() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("C1");
        Category child = new Category();
        child.setId(2L);
        child.setName("C2");
        category.setChildCategories(Collections.singleton(child));

        // Testing success scenario
        Mockito.when(categoryService.getCategoryById(Mockito.eq(1L))).thenReturn(Optional.of(category));
        Mockito.when(categoryService.getCategoryById(Mockito.eq(2L))).thenReturn(Optional.of(child));
        Mockito.when(categoryService.isChildCategory(Mockito.eq(child), Mockito.eq(category))).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{childid}", "1", "2"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent());
        Mockito.verify(categoryService).removeChildCategory(Mockito.eq(child), Mockito.eq(category));

        Mockito.reset(categoryService);

        // Testing fail scenario: subcategory is not associated with parent category
        Mockito.when(categoryService.getCategoryById(Mockito.eq(1L))).thenReturn(Optional.of(category));
        Mockito.when(categoryService.getCategoryById(Mockito.eq(2L))).thenReturn(Optional.of(child));
        Mockito.when(categoryService.isChildCategory(Mockito.eq(child), Mockito.eq(category))).thenReturn(true);
        Mockito.when(categoryService.getCategoryById(Mockito.eq(1L))).thenReturn(Optional.of(category));
        Mockito.when(categoryService.getCategoryById(Mockito.eq(2L))).thenReturn(Optional.of(child));
        Mockito.when(categoryService.isChildCategory(Mockito.eq(child), Mockito.eq(category))).thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{childid}", "1", "2"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
        Mockito.verify(categoryService, Mockito.never()).addChildCategory(Mockito.eq(child), Mockito.eq(category));
    }

    @Test
    public void testResourceNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL, "0"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders.post(URL + "/{childid}", "0", "1"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{childid}", "0", "1"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
