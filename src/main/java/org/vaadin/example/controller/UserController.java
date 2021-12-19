package org.vaadin.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.security.Principal;

@RestController
@PermitAll
public class UserController {

    @GetMapping("/principal")
    public Principal getPrincipal(Principal principal) {
        return principal;
    }
}
