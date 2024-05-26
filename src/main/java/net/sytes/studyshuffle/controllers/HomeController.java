package net.sytes.studyshuffle.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//for Angular Client (withCredentials)
//@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/api/home/user")
public class HomeController {
  @GetMapping("/all")
  public String allAccess() {
    return "Public Content.";
  }

  @GetMapping("/student")
  @PreAuthorize("hasRole('STUDENT')")
  public String userAccess() {
    return "student";
  }

  @GetMapping("/teacher")
  @PreAuthorize("hasRole('TEACHER')")
  public String moderatorAccess() {
    return "teacher";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {
    return "admin";
  }


}
