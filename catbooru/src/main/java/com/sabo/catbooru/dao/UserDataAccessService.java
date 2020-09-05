package com.sabo.catbooru.dao;

import com.sabo.catbooru.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public class UserDataAccessService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void registerNewUser(User user){
        final String sql = "INSERT INTO users (id, username, password) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, user.getId(), user.getUsername(), user.getPassword());
    }

    public User getUserByUsername(String username){
            final String sql = "SELECT * FROM users WHERE username = ?";
            try{
                return jdbcTemplate.queryForObject(sql, new Object[]{username}, (resultSet, i) -> {
                    UUID personId = UUID.fromString(resultSet.getString("id"));
                    String name = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    return new User(personId, name, password);
                });
            }
            catch (DataAccessException e){
                User user = null;
                return user;
            }

    }

    public String getUsernameById(UUID id){
        final String sql = "SELECT * FROM users WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (resultSet, i) -> resultSet.getString("username"));
    }

    public void deleteUserById(UUID id){
        final String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void changeUsernamePassword(UUID id, User user){
        final String sql = "UPDATE users SET username = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), id);
    }

}
