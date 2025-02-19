package controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetController {

    //allow everyone access
    @GetMapping("/")
    public String greet() {
        return "Welcome!";
    }

    //only for user
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public String helloUser() {
        return "Hello, User!";
    }

    //only for admin
    @PreAuthorize("hashRole('ADMIN')")
    @GetMapping("/admin")
    public String helloAdmin() {
        return "Hello, Admin!";
    }
}
