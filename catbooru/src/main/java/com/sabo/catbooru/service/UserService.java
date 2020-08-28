package com.sabo.catbooru.service;

import com.sabo.catbooru.dao.UserDataAccessService;
import com.sabo.catbooru.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;


@Service
public class UserService implements UserDetailsService {

    private UserDataAccessService userDataAccessService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired PostService postService;

    @Autowired
    public UserService(UserDataAccessService userDataAccessService) {
        this.userDataAccessService = userDataAccessService;
    }

    public UUID registerNewUser(User user){
        user.generateId();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDataAccessService.registerNewUser(user);
        return user.getId();
    }

    public void deleteUserById(UUID id){
        postService.deleteUserPosts(id);
        userDataAccessService.deleteUserById(id);
    }

    public void changeUsernamePassword(UUID id, User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDataAccessService.changeUsernamePassword(id, user);
    }

    public User getUserByUsername(String username){
        return userDataAccessService.getUserByUsername(username);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDataAccessService.getUserByUsername(username);
        return new org.springframework.security.core.userdetails.User (user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}
