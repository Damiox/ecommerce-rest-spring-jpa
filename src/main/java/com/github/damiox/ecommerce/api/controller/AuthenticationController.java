package com.github.damiox.ecommerce.api.controller;

import com.github.damiox.ecommerce.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RestController
public class AuthenticationController {

    @Autowired
    private SecurityService securityService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody @Valid CredentialsDto credentialsDto) {
        final String username = credentialsDto.getUsername();
        final String password = credentialsDto.getPassword();

        // Authenticating...
        final String token = securityService.authenticate(username, password);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    public static class CredentialsDto {
        @NotNull(message = "username is required")
        @Size(message = "username must be equal to or lower than 50", min = 1, max = 50)
        private String username;
        @NotNull(message = "password is required")
        @Size(message = "password must be equal to or lower than 50", min = 1, max = 50)
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class AuthenticationResponse {
        private final String token;

        public AuthenticationResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }

}
