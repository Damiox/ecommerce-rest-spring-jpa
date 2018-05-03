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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

/**
 * Unit tests for {@link CategoryController}
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = CategoryController.class, secure = false, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JWTAuthenticationFilter.class))
public class CategoryControllerTest {

    private static final String URL = "/categories";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CategoryService categoryService;
    @SpyBean
    private CategoryResourceAssembler categoryResourceAssembler;
    @MockBean
    private ProductService productService;

    @Test
    public void testRetrieveAllCategories() throws Exception {
        // categories
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("C1");
        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("C2");
        Category category3 = new Category();
        category3.setId(3L);
        category3.setName("C3");
        Category category4 = new Category();
        category4.setId(4L);
        category4.setName("C4");
        Category category5 = new Category();
        category5.setId(5L);
        category5.setName("C5");

        // parent-child associations
        category1.setChildCategories(Collections.singleton(category2));
        category2.setParent(category1);
        category3.setChildCategories(Collections.singleton(category4));
        category4.setParent(category3);

        Mockito.when(productService.hasProductsAssociated(Mockito.eq(category1))).thenReturn(true);
        Mockito.when(productService.hasProductsAssociated(Mockito.eq(category2))).thenReturn(true);
        Mockito.when(productService.hasProductsAssociated(Mockito.eq(category4))).thenReturn(true);

        Mockito.when(categoryService.getAllCategories()).thenReturn(Arrays.asList(category1, category2, category3, category4, category5));

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            // Testing 5 categories
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(5)))
            // Category 1: no parent,, it has subcategories, is has products
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name", Matchers.is("C1")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].links[0].rel", Matchers.is("self")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].links[0].href", Matchers.is("http://localhost/categories/1")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].links[1].rel", Matchers.is("subcategories")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].links[1].href", Matchers.is("http://localhost/categories/1/subcategories")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].links[2].rel", Matchers.is("products")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].links[2].href", Matchers.is("http://localhost/categories/1/products")))
            // Category 2: parent=category 1,, it has subcategories, is has products
            .andExpect(MockMvcResultMatchers.jsonPath("$.[1].name", Matchers.is("C2")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[1].links[0].rel", Matchers.is("self")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[1].links[0].href", Matchers.is("http://localhost/categories/2")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[1].links[1].rel", Matchers.is("parent")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[1].links[1].href", Matchers.is("http://localhost/categories/1")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[1].links[2].rel", Matchers.is("products")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[1].links[2].href", Matchers.is("http://localhost/categories/2/products")))
            // Category 3: no parent, it has subcategories, no products
            .andExpect(MockMvcResultMatchers.jsonPath("$.[2].name", Matchers.is("C3")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[2].links[0].rel", Matchers.is("self")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[2].links[0].href", Matchers.is("http://localhost/categories/3")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[2].links[1].rel", Matchers.is("subcategories")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[2].links[1].href", Matchers.is("http://localhost/categories/3/subcategories")))
            // Category 4: parent=categoy 5, no subcategories, it contains products
            .andExpect(MockMvcResultMatchers.jsonPath("$.[3].name", Matchers.is("C4")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[3].links[0].rel", Matchers.is("self")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[3].links[0].href", Matchers.is("http://localhost/categories/4")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[3].links[1].rel", Matchers.is("parent")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[3].links[1].href", Matchers.is("http://localhost/categories/3")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[3].links[2].rel", Matchers.is("products")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[3].links[2].href", Matchers.is("http://localhost/categories/4/products")))
            // Category 5: no parent, no subcategories, no products
            .andExpect(MockMvcResultMatchers.jsonPath("$.[4].name", Matchers.is("C5")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[4].links[0].rel", Matchers.is("self")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.[4].links[0].href", Matchers.is("http://localhost/categories/5")));
    }

    @Test
    public void testRetrieveAllCategoriesEmpty() throws Exception {
        Mockito.when(categoryService.getAllCategories()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.empty()));
    }

    @Test
    public void testRetrieveCategory() throws Exception {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("C1");
        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("C2");

        // parent-child associations
        category1.setChildCategories(Collections.singleton(category2));
        category2.setParent(category1);

        Mockito.when(productService.hasProductsAssociated(Mockito.eq(category2))).thenReturn(true);
        Mockito.when(categoryService.getCategoryById(Mockito.eq(2L))).thenReturn(Optional.of(category2));

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{categoryid}", "2"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("C2")))
            .andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href", Matchers.is("http://localhost/categories/2")))
            .andExpect(MockMvcResultMatchers.jsonPath("$._links.parent.href", Matchers.is("http://localhost/categories/1")))
            .andExpect(MockMvcResultMatchers.jsonPath("$._links.products.href", Matchers.is("http://localhost/categories/2/products")));
    }

    @Test
    public void testCreateCategory() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("C1");

        Mockito.when(categoryService.createCategory(Mockito.eq("C1"))).thenReturn(category);

        String request = "{ \"name\": \"C1\" }";
        mockMvc.perform(MockMvcRequestBuilders.post(URL).content(request).contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("C1")))
            .andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href", Matchers.is("http://localhost/categories/1")));

        Mockito.verify(categoryService).createCategory(Mockito.eq("C1"));
    }

    @Test
    public void testUpdateCategory() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("C1");

        Mockito.when(categoryService.getCategoryById(Mockito.eq(1L))).thenReturn(Optional.of(category));
        Mockito.doNothing().when(categoryService).updateCategory(Mockito.eq(category), Mockito.eq("C1"));

        String request = "{ \"name\": \"C1\" }";
        mockMvc.perform(MockMvcRequestBuilders.put(URL + "/{categoryid}", "1").content(request).contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("C1")))
            .andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href", Matchers.is("http://localhost/categories/1")));

        Mockito.verify(categoryService).updateCategory(Mockito.eq(category), Mockito.eq("C1"));
    }

    @Test
    public void testDeleteCategory() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("C1");

        Mockito.when(categoryService.getCategoryById(Mockito.eq(1L))).thenReturn(Optional.of(category));
        Mockito.doNothing().when(categoryService).deleteCategory(Mockito.eq(category));

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{categoryid}", "1"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(categoryService).deleteCategory(Mockito.eq(category));
    }

    @Test
    public void testResourceNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{categoryid}", "0"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders.put(URL + "/{categoryid}", "0").content("{ \"name\": \"name\" }").contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{categoryid}", "0"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
