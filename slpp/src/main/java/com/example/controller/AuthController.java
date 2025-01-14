package com.example.controller;

import com.example.dto.UserDTO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(Authentication authentication) {
        // If user is already authenticated, redirect to dashboard
        if (authentication != null && authentication.isAuthenticated() &&
                !authentication.getName().equals("anonymousUser")) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    @GetMapping("/register")
    public String register(Authentication authentication, Model model) {
        // If user is already authenticated, redirect to dashboard
        if (authentication != null && authentication.isAuthenticated() &&
                !authentication.getName().equals("anonymousUser")) {
            return "redirect:/dashboard";
        }
        model.addAttribute("userDTO", new UserDTO());
        return "register";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }
}