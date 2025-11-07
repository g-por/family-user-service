package com.familybudget.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HelloController {

    @GetMapping("/api/hello")
    public String hello() {
        return "user-service";
    }
}
