package com.github.damiox.ecommerce.api.controller;

import com.github.damiox.ecommerce.api.assembler.CategoryResourceAssembler;
import com.github.damiox.ecommerce.api.assembler.ProductResourceAssembler;
import com.github.damiox.ecommerce.entity.Category;
import com.github.damiox.ecommerce.entity.Product;
import com.github.damiox.ecommerce.entity.User;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

/**
 * Unit tests for {@link CategoryProductsController}
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = CategoryProductsController.class, secure = false, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JWTAuthenticationFilter.class))
@EnableSpringDataWebSupport
public class CategoryProductsControllerTest {

    private static final String URL = "/categories/{categoryid}/products";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CategoryService categoryService;
    @SpyBean
    private CategoryResourceAssembler categoryResourceAssembler;
    @SpyBean
    private ProductResourceAssembler productResourceAssembler;
    @SpyBean
    private PagedResourcesAssembler<Product> pagedResourcesAssembler;
    @MockBean
    private ProductService productService;

    @Test
    public void testRetrieveAllProducts() throws Exception {
        User user = new User();
        user.setUsername("damiox");
        Category category = new Category();
        category.setId(1L);
        category.setName("C1");
        Product product1 = new Product();
        product1.setId(10L);
        product1.setName("P1");
        product1.setPrice(100.00);
        product1.setCategories(Collections.singleton(category));
        product1.setUser(user);
        Product product2 = new Product();
        product2.setId(11L);
        product2.setName("P2");
        product2.setPrice(130.67);
        product2.setCategories(Collections.singleton(category));
        product2.setUser(user);

        Mockito.when(categoryService.getCategoryById(Mockito.eq(1L))).thenReturn(Optional.of(category));
        Mockito.when(productService.getAllProducts(Mockito.eq(category), Mockito.any())).thenReturn(
            new PageImpl<>(Arrays.asList(product1, product2), PageRequest.of(2, 20), 100)
        );

        mockMvc.perform(MockMvcRequestBuilders.get(URL, "1"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.productResourceList", Matchers.hasSize(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.productResourceList[0].name", Matchers.is("P1")))
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.productResourceList[0].currency", Matchers.is("EUR")))
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.productResourceList[0].price", Matchers.is(100.0)))
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.productResourceList[0].categories", Matchers.hasSize(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.productResourceList[0].categories[0].name", Matchers.is("C1")))
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.productResourceList[0].categories[0]._links.self.href", Matchers.is("http://localhost/categories/1")))
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.productResourceList[0].owner", Matchers.is("damiox")))
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.productResourceList[0]._links.self.href", Matchers.is("http://localhost/products/10")))
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.productResourceList[1].name", Matchers.is("P2")))
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.productResourceList[1].currency", Matchers.is("EUR")))
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.productResourceList[1].price", Matchers.is(130.67)))
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.productResourceList[1].categories", Matchers.hasSize(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.productResourceList[1].categories[0].name", Matchers.is("C1")))
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.productResourceList[1].categories[0]._links.self.href", Matchers.is("http://localhost/categories/1")))
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.productResourceList[1].owner", Matchers.is("damiox")))
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.productResourceList[1]._links.self.href", Matchers.is("http://localhost/products/11")))
            .andExpect(MockMvcResultMatchers.jsonPath("$._links.first.href", Matchers.is("http://localhost/categories/1/products?page=0&size=20")))
            .andExpect(MockMvcResultMatchers.jsonPath("$._links.prev.href", Matchers.is("http://localhost/categories/1/products?page=1&size=20")))
            .andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href", Matchers.is("http://localhost/categories/1/products?page=2&size=20")))
            .andExpect(MockMvcResultMatchers.jsonPath("$._links.next.href", Matchers.is("http://localhost/categories/1/products?page=3&size=20")))
            .andExpect(MockMvcResultMatchers.jsonPath("$._links.last.href", Matchers.is("http://localhost/categories/1/products?page=4&size=20")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.page.size", Matchers.is(20)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements", Matchers.is(100)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalPages", Matchers.is(5)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.page.number", Matchers.is(2)));
    }

    @Test
    public void testAddProduct() throws Exception {
        User user = new User();
        user.setUsername("damiox");
        Category category = new Category();
        category.setId(1L);
        category.setName("C1");
        Product product = new Product();
        product.setId(10L);
        product.setName("P1");
        product.setPrice(20.18);
        product.setUser(user);

        Mockito.when(categoryService.getCategoryById(Mockito.eq(1L))).thenReturn(Optional.of(category));

        // Testing success scenario
        Mockito.when(productService.getProductById(Mockito.eq(10L))).thenReturn(Optional.of(product));
        Mockito.when(productService.hasCategory(Mockito.eq(product), Mockito.eq(category))).thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders.post(URL + "/{productid}", "1", "10"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("P1")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.currency", Matchers.is("EUR")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.price", Matchers.is(20.18)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.owner", Matchers.is("damiox")))
            .andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href", Matchers.is("http://localhost/products/10")));
        Mockito.verify(productService).addCategory(Mockito.eq(product), Mockito.eq(category));

        Mockito.reset(productService);

        // Testing fail scenario: product is already associated with category
        Mockito.when(productService.getProductById(Mockito.eq(10L))).thenReturn(Optional.of(product));
        Mockito.when(productService.hasCategory(Mockito.eq(product), Mockito.eq(category))).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.post(URL + "/{productid}", "1", "10"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
        Mockito.verify(productService, Mockito.never()).addCategory(Mockito.eq(product), Mockito.eq(category));
    }

    @Test
    public void testRemoveProduct() throws Exception {
        User user = new User();
        user.setUsername("damiox");
        Category category = new Category();
        category.setId(1L);
        category.setName("C1");
        Product product = new Product();
        product.setId(10L);
        product.setName("P1");
        product.setPrice(20.18);
        product.setUser(user);

        Mockito.when(categoryService.getCategoryById(Mockito.eq(1L))).thenReturn(Optional.of(category));

        // Testing success scenario
        Mockito.when(productService.getProductById(Mockito.eq(10L))).thenReturn(Optional.of(product));
        Mockito.when(productService.hasCategory(Mockito.eq(product), Mockito.eq(category))).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{productid}", "1", "10"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent());
        Mockito.verify(productService).removeCategory(Mockito.eq(product), Mockito.eq(category));

        Mockito.reset(productService);

        // Testing fail scenario: product is not associated with category
        Mockito.when(productService.getProductById(Mockito.eq(10L))).thenReturn(Optional.of(product));
        Mockito.when(productService.hasCategory(Mockito.eq(product), Mockito.eq(category))).thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{productid}", "1", "10"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
        Mockito.verify(productService, Mockito.never()).removeCategory(Mockito.eq(product), Mockito.eq(category));
    }

    @Test
    public void testResourceNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL, "0"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders.post(URL + "/{productid}", "0", "1"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{productid}", "0", "1"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
