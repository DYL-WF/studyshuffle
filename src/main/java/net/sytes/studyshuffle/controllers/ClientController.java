package net.sytes.studyshuffle.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
    public String Home()  {
        return "index";
    }

    @GetMapping("/index")
    public String Index() {
        return "index";
    }


}