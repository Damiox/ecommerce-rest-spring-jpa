package com.github.damiox.ecommerce.security;

import com.github.damiox.ecommerce.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * JWT Authentication Filter
 * If an 'Authorization' header is found in the HTTP Request then the token is being retrieved.
 * That token will be validated and, if successful, then user will be authenticated.
 *
 * @author dnardelli
 */
@Component
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String HEADER_STRING = "Authorization";

    @Autowired
    private SecurityService securityService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostConstruct
    private void initialize() {
        this.setAuthenticationManager(authenticationManager);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {

        final HttpServletRequest httpRequest = HttpServletRequest.class.cast(request);
        final String token = httpRequest.getHeader(HEADER_STRING);

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            securityService.authenticate(token);
        }

        filterChain.doFilter(request, response);
    }

}