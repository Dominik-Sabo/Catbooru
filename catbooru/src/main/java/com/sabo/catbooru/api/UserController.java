package com.sabo.catbooru.api;

import com.sabo.catbooru.model.AuthenticationRequest;
import com.sabo.catbooru.model.User;
import com.sabo.catbooru.service.UserService;
import com.sabo.catbooru.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtTokenUtil;
    
    @PostMapping("/register")
    public ResponseEntity<User> registerNewUser(@RequestBody User user) {
        user.generateId();
        userService.registerNewUser(user);
        String jwt = generateJwt(user);
        user.setToken(jwt);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> deleteUserByUsername(@PathVariable UUID id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<User> changeUsernamePassword(@PathVariable UUID id, @RequestBody User user, HttpServletResponse response) {
        userService.changeUsernamePassword(id, user);
        String jwt = generateJwt(user);
        user.setToken(jwt);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<User> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        User user = userService.getUserByUsername(authenticationRequest.getUsername());

        String jwt = generateJwt(user);
        user.setToken(jwt);
        user.setPassword(null);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/outlog")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().build();
    }

    private void addAuthenticationCookie(String bearer, HttpServletResponse response) {
        Cookie cookie = new Cookie("authentication", (bearer));
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 6);
        response.addCookie(cookie);
    }

    private String generateJwt(User user){
        final UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
        return jwtTokenUtil.generateToken(userDetails);
    }


}

