package com.example.INGStoreManagement.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/login")
public class AuthController {

    @GetMapping()
    public ResponseEntity<Void> login(HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        return ResponseEntity
                .noContent()
                .header(csrfToken.getHeaderName(), csrfToken.getToken())
                .build();
    }
}
