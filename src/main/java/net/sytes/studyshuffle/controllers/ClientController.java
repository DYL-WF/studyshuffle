package net.sytes.studyshuffle.controllers;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ClientController {
    @GetMapping("/login")
    public String Login() {
        return "login";
    }

    @GetMapping("/register")
    public String Register() {
        return "register";
    }

    @GetMapping("/")
    public void Index(HttpServletResponse httpResponse) throws IOException {
        httpResponse.sendRedirect("/home");
    }



}