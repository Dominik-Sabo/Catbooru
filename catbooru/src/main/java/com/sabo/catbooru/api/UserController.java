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
public class UserController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @GetMapping("/hello")
    public void hello(@RequestParam String username) {
        userService.getUserByUsername(username);
    }

    @PostMapping("/register")
    public ResponseEntity<UUID> registerNewUser(@RequestBody User user, HttpServletResponse response) {
        UUID userId = userService.registerNewUser(user);
        this.addAuthenticationCookie("Bearer" + generateJwt(user), response);
        return ResponseEntity.ok().body(userId);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> deleteUserByUsername(@PathVariable UUID id, HttpServletResponse response) {
        userService.deleteUserById(id);
        addAuthenticationCookie("", response);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<?> changeUsernamePassword(@PathVariable UUID id, @RequestBody User user, HttpServletResponse response) {
        userService.changeUsernamePassword(id, user);
        this.addAuthenticationCookie("Bearer" + generateJwt(user), response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<UUID> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        User user = userService.getUserByUsername(authenticationRequest.getUsername());

        this.addAuthenticationCookie("Bearer" + generateJwt(user), response);

        return ResponseEntity.ok().body(user.getId());
    }

    @PostMapping("/outlog")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        addAuthenticationCookie("", response);
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

