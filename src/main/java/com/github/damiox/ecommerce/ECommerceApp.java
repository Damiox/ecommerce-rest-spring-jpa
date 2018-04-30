package com.github.damiox.ecommerce;

import com.github.damiox.ecommerce.security.EntryPointUnauthorizedHandler;
import com.github.damiox.ecommerce.security.JWTAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Boot App Runner
 *
 * @author dnardelli
 */
@SpringBootApplication
public class ECommerceApp {

    public static void main(String[] args) {
        SpringApplication.run(ECommerceApp.class, args);
    }

    @Configuration
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public static class SecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private JWTAuthenticationFilter authenticationFilter;
        @Autowired
        private EntryPointUnauthorizedHandler unauthorizedHandler;
        @Autowired
        private UserDetailsService userDetailsService;

        @Override
        protected void configure(AuthenticationManagerBuilder security) throws Exception {
            security
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(passwordEncoder());
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // 1) Enabling Frames because it's required for h2. Note: this shouldn't be exposed to prod.
            // 2) Setting exception handler in Filters
            // 3) Allowing the following actions without authentication:
            // 3.a) Processing OPTIONS for all endpoints
            // 3.b) Processing POST on /login
            // 3.c) Accessing to h2 web console. Note: this shouldn't be exposed to prod.
            // 4) All the other requests need to be authenticated
            // 5) Adding to the filter chain the JWT Authentication Filter
            http
                .csrf()
                    .disable()
                .headers()
                    .frameOptions()
                    .sameOrigin()
                    .and()
                .exceptionHandling()
                    .authenticationEntryPoint(this.unauthorizedHandler)
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/login").permitAll()
                    .antMatchers("/h2", "/h2/**", "/error").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

    }

}
