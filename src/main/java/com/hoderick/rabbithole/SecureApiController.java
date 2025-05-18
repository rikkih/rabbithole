package com.hoderick.rabbithole;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SecureApiController {

    @GetMapping("/secure")
    public String secureEndpoint(Authentication authentication) {
        return "Hello " + authentication.getName() + ", you have accessed a secure endpoint!";
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This endpoint is public.";
    }
}
