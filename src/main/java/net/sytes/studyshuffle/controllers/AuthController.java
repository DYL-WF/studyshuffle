package net.sytes.studyshuffle.controllers;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import net.sytes.studyshuffle.models.ERole;
import net.sytes.studyshuffle.models.Role;
import net.sytes.studyshuffle.models.User;
import net.sytes.studyshuffle.payload.request.LoginRequest;
import net.sytes.studyshuffle.payload.request.RegisterRequest;
import net.sytes.studyshuffle.payload.request.SignupRequest;
import net.sytes.studyshuffle.repository.RoleRepository;
import net.sytes.studyshuffle.repository.UserRepository;
import net.sytes.studyshuffle.security.jwt.JwtUtils;
import net.sytes.studyshuffle.security.services.UserDetailsImpl;


//for Angular Client (withCredentials)
//@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path="/api/auth")
public class AuthController {
  @Autowired
  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping(path="/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody @ModelAttribute LoginRequest loginRequest, Model model) {
    HttpHeaders headers = new HttpHeaders();

    logger.info("{} is attempting to login", loginRequest.getUsername());
    
    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    String redirect="/login";
    if(roles.get(0)=="ROLE_STUDENT"){
      Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
      if(user.get().getRegistered()){
        redirect ="/api/home/user/student?registered";
      }
      else{
        redirect ="/api/home/user/student?notregistered&username="+userDetails.getUsername();
      }
    }else if(roles.get(0)=="ROLE_TEACHER"){
      List<User> registredUsers = userRepository.findByRegisteredTrue();
      List<String> students = new ArrayList<String>();
      for (User u : registredUsers) {
        logger.info("{}", u.getUsername());
        students.add(u.getUsername());
      }
      redirect ="/api/home/user/teacher";
      redirect += "?student=" + students.stream().collect(Collectors.joining("&student="));
    }else if(roles.get(0)=="ROLE_ADMIN"){
      redirect ="/api/home/user/admin";
    }
    headers.setLocation(URI.create(redirect));
    headers.add(HttpHeaders.SET_COOKIE, jwtCookie.toString());
    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);

    // return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).header("Location", "/api/test/").body(new UserInfoResponse(userDetails.getId(),
    //         userDetails.getUsername(),
    //         userDetails.getEmail(),
    //         roles));

  }

  @PostMapping(path="/register")
  public ResponseEntity<?> registerUser(@Valid @RequestBody @ModelAttribute SignupRequest signUpRequest ) {
    logger.info("A user is attempting to register");
    HttpHeaders headers = new HttpHeaders();
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      logger.info("Id number is already taken!");

      headers.setLocation(URI.create("/register?registration_error=Error%3A%20Username%20number%20is%20already%20taken%21"));

      return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      logger.info("Email is already in use!");
      headers.setLocation(URI.create("/register?registration_error=Email%20is%20already%20in%20use%21"));

      return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }
    if (signUpRequest.getPassword().length() < 6) {
      logger.info("Password is too short!");
      headers.setLocation(URI.create("/register?registration_error=Error%3A%20Password%20is%20too%20short%21"));

      return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    try {
      // Create new user's account
      User user = new User(signUpRequest.getName(),
      signUpRequest.getUsername(),
      signUpRequest.getEmail(),
      encoder.encode(signUpRequest.getPassword()));

      Set<String> strRoles = signUpRequest.getRole();
      Set<Role> roles = new HashSet<>();


      logger.info("Role is set to {} ", signUpRequest.getRole());
      if (strRoles == null) {
        Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);
      } else {
        strRoles.forEach(role -> {
          switch (role) {
            case "teacher":
              Role teacherRole = roleRepository.findByName(ERole.ROLE_TEACHER)
                  .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
              roles.add(teacherRole);

              break;
            case "admin":
              Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                  .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
              roles.add(adminRole);

              break;
            default:
              Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                  .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
              roles.add(userRole);
          }
        });
      }

      user.setRoles(roles);
      userRepository.save(user);
      logger.info("User has been saved to database ", signUpRequest.getRole());

      headers.setLocation(URI.create("/login"));

      return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);

    } catch (Throwable e) {
      logger.error( "Something went wrong",e);
      headers.setLocation(URI.create("/register?registration_error=Error%3A%20something%20went%20wrong"));

      return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }
    
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logoutUser() {
    logger.info("A user has logged out");
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    HttpHeaders headers = new HttpHeaders();

    headers.setLocation(URI.create("/login"));
    headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
  }

  @PostMapping("/registercourse")
  public ResponseEntity<?> registerCourse(@Valid @RequestBody @ModelAttribute RegisterRequest registerRequest) {

    logger.info("User {} is attempting to register for the module",registerRequest.getUsername());
    Optional<User> user = userRepository.findByUsername(registerRequest.getUsername());
    HttpHeaders headers = new HttpHeaders();

    user.get().setRegistered(true);
    userRepository.save(user.get());
    headers.setLocation(URI.create("/api/home/user/student?registered=yes"));
    logger.info("User {} has successfully registered for the module", registerRequest.getUsername());
    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
  }
}
