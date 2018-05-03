package com.github.damiox.ecommerce.api.controller;

import com.github.damiox.ecommerce.service.SecurityService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Unit tests for {@link AuthenticationController}
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = AuthenticationController.class, secure = false)
public class AuthenticationControllerTest {

    private static final String URL = "/login";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SecurityService securityService;

    @Test
    public void testLogin() throws Exception {
        Mockito.when(securityService.authenticate(Mockito.eq("user"), Mockito.eq("user"))).thenReturn("secret");

        String request = "{ \"username\": \"user\", \"password\": \"user\" }";
        mockMvc.perform(MockMvcRequestBuilders.post(URL).content(request).contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.token", Matchers.is("secret")));
    }

    @Test
    public void testInvalidUsername() throws Exception {
        Mockito.when(securityService.authenticate(Mockito.eq("user"), Mockito.eq("user"))).thenReturn("secret");

        String request;

        request = "{ \"username\": null, \"password\": \"user\" }";
        mockMvc.perform(MockMvcRequestBuilders.post(URL).content(request).contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

        request = "{ \"username\": \"\", \"password\": \"user\" }";
        mockMvc.perform(MockMvcRequestBuilders.post(URL).content(request).contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

        request = "{ \"username\": \"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\", \"password\": \"user\" }";
        mockMvc.perform(MockMvcRequestBuilders.post(URL).content(request).contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testInvalidPassword() throws Exception {
        Mockito.when(securityService.authenticate(Mockito.eq("user"), Mockito.eq("user"))).thenReturn("secret");

        String request;

        request = "{ \"username\": \"user\", \"password\": null }";
        mockMvc.perform(MockMvcRequestBuilders.post(URL).content(request).contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

        request = "{ \"username\": \"user\", \"password\": \"\" }";
        mockMvc.perform(MockMvcRequestBuilders.post(URL).content(request).contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

        request = "{ \"username\": \"user\", \"password\": \"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\" }";
        mockMvc.perform(MockMvcRequestBuilders.post(URL).content(request).contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}
