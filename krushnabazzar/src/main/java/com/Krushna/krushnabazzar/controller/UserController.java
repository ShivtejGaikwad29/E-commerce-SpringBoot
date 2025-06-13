package com.Krushna.krushnabazzar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("faillogin")
    public String failLogin() {
        return "faillogin"; // This should match your filename: faillogin.html
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
