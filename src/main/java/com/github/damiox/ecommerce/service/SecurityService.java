package com.github.damiox.ecommerce.service;

import org.springframework.security.core.Authentication;

public interface SecurityService {

    String authenticate(final String username, final String password);

    Authentication authenticate(final String token);

}
