package com.sabo.catbooru.dao;

import com.sabo.catbooru.model.Comment;
import com.sabo.catbooru.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;


@Repository
public class PostDataAccessService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createNewPost(Post post){
        String sql = "INSERT INTO posts (id, userId, filePath, dateUploaded) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
        jdbcTemplate.update(sql, post.getId(), post.getUserId(), post.getFilePath());
    }

    public void addPostTags(UUID id, String[] tags){
        String sql = "INSERT INTO tags (tag, postId) VALUES (?, ?)";
        for(String tag : tags){
            jdbcTemplate.update(sql, tag, id);
        }
    }

    public void deletePostById(UUID id){
        String sql = "DELETE FROM posts WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void deletePostTags(UUID id){
        String sql = "DELETE FROM tags WHERE postid = ?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteTag(UUID id, String tag){
        String sql = "DELETE FROM tags WHERE postId = ? AND tag = ?";
        jdbcTemplate.update(sql, id, tag);
    }

    public List<String> getPostTags(UUID id){
        String sql = "SELECT * FROM tags WHERE postId = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, (resultSet, i) -> {
            return resultSet.getString("tag");
        });
    }

    public List<Post> getAllPosts(){
        String sql = "SELECT * FROM posts";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            Post post = new Post();
            post.setFilePath(resultSet.getString("filePath"));
            post.setId(UUID.fromString(resultSet.getString("id")));
            post.setUpvotes(resultSet.getInt("upvotes"));
            post.setTime(resultSet.getTimestamp("dateUploaded"));
            return post;
        });
    }

    public List<Post> filterPosts(String[] query){
        String sql = "SELECT * FROM tags WHERE tag = ?";
        List<UUID> filteredPostIds = new ArrayList<UUID>();
        List<UUID> idsToRetain = new ArrayList<UUID>();
        for(int i = 0; i < query.length; i++) {
            for(UUID id : (jdbcTemplate.query(sql, new Object[]{query[i]}, (resultSet, j) -> UUID.fromString(resultSet.getString("postId"))))){
                if(i == 0) filteredPostIds.add(id);
                idsToRetain.add(id);
            }
            if(i>0) filteredPostIds.retainAll(idsToRetain);
            idsToRetain.clear();
        }
        sql = "SELECT * FROM posts WHERE id = ?";
        List<Post> filteredPosts = new ArrayList<Post>();
        for(UUID id : filteredPostIds){
            filteredPosts.addAll(jdbcTemplate.query(sql, new Object[]{id}, (resultSet, i) -> {
                Post post = new Post();
                post.setFilePath(resultSet.getString("filePath"));
                post.setId(UUID.fromString(resultSet.getString("id")));
                post.setUpvotes(resultSet.getInt("upvotes"));
                post.setTime(resultSet.getTimestamp("dateUploaded"));
                return post;
            }));
        }
        return filteredPosts;
    }

    public Optional<Post> getPost(UUID id){
        String sql = "SELECT * FROM posts WHERE id = ?";
        Post post = jdbcTemplate.queryForObject(sql, new Object[]{id}, (resultSet, i) -> {
            Post selectedPost = new Post();
            selectedPost.setId(UUID.fromString(resultSet.getString("id")));
            selectedPost.setUserId(UUID.fromString(resultSet.getString("userId")));
            selectedPost.setFilePath(resultSet.getString("filePath"));
            selectedPost.setUpvotes(resultSet.getInt("upvotes"));
            selectedPost.setTime(resultSet.getTimestamp("dateUploaded"));
            return selectedPost;
        });
        return Optional.ofNullable(post);
    }

    public List<Post> getUserPosts(UUID userId){
        String sql = "SELECT * FROM posts WHERE userId = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, (resultSet, i) -> {
            Post post = new Post();
            post.setFilePath(resultSet.getString("filePath"));
            post.setId(UUID.fromString(resultSet.getString("id")));
            post.setUpvotes(resultSet.getInt("upvotes"));
            post.setTime(resultSet.getTimestamp("dateUploaded"));
            return post;
        });
    }

    public List<Post> getLikedPosts(UUID userId){
        String sql = "SELECT * FROM upvotes WHERE userId = ?";
        List<UUID> postIds = new ArrayList<>(jdbcTemplate.query(sql, new Object[]{userId}, (resultSet, i) -> UUID.fromString(resultSet.getString("postId"))));
        sql = "SELECT * FROM posts WHERE id = ?";
        List<Post> posts = new ArrayList<>();
        for(UUID postId : postIds) {
            posts.addAll(jdbcTemplate.query(sql, new Object[]{postId}, (resultSet, i) -> {
                Post post = new Post();
                post.setFilePath(resultSet.getString("filePath"));
                post.setId(UUID.fromString(resultSet.getString("id")));
                post.setUpvotes(resultSet.getInt("upvotes"));
                post.setTime(resultSet.getTimestamp("dateUploaded"));
                return post;
            }));
        }
        return posts;
    }

    public void commentOnPost(Comment comment){
        String sql = "INSERT INTO comments(id, postId, userId, commentText, timeCommented) VALUES(?, ?, ?, ?, CURRENT_TIMESTAMP)";
        jdbcTemplate.update(sql, comment.getId(), comment.getPostId(), comment.getUserId(), comment.getCommentText());
    }

    public void deleteCommentById(UUID commentId){
        String sql = "DELETE FROM comments WHERE id = ?";
        jdbcTemplate.update(sql, commentId);
    }

    public List<Comment> getPostComments(UUID postId){
        String sql = "SELECT * FROM comments WHERE postId = ?";
        return jdbcTemplate.query(sql, new Object[]{postId}, (resultSet, i) -> {
            Comment comment = new Comment();
            comment.setId(UUID.fromString(resultSet.getString("id")));
            comment.setPostId(UUID.fromString(resultSet.getString("postId")));
            comment.setUserId(UUID.fromString(resultSet.getString("userId")));
            comment.setCommentText(resultSet.getString("commentText"));
            comment.setTime(resultSet.getTimestamp("timeCommented"));
            return comment;
        });
    }

    public void deletePostComments(UUID postId){
        String sql = "DELETE FROM comments WHERE postid = ?";
        jdbcTemplate.update(sql, postId);
    }

    public List<Post> getCommentedOnPosts(UUID userId){
        String sql = "SELECT * FROM comments WHERE userId = ?";
        List<UUID> postIds = new ArrayList<>(jdbcTemplate.query(sql, new Object[]{userId}, (resultSet, i) -> UUID.fromString(resultSet.getString("postId"))).stream().distinct().collect(Collectors.toList()));
        sql = "SELECT * FROM posts WHERE id = ?";
        List<Post> posts = new ArrayList<>();
        for(UUID postId : postIds) {
            posts.addAll(jdbcTemplate.query(sql, new Object[]{postId}, (resultSet, i) -> {
                Post post = new Post();
                post.setFilePath(resultSet.getString("filePath"));
                post.setId(UUID.fromString(resultSet.getString("id")));
                post.setUpvotes(resultSet.getInt("upvotes"));
                post.setTime(resultSet.getTimestamp("dateUploaded"));
                return post;
            }));
        }
        return posts;
    }

    public void likePost(UUID userId, UUID postId){
        String sql = "UPDATE posts SET upvotes = upvotes + 1 WHERE id = ?";
        jdbcTemplate.update(sql, postId);
        sql = "INSERT INTO upvotes(postId, userId) VALUES(?, ?)";
        jdbcTemplate.update(sql, postId, userId);
    }

    public void unlikePost(UUID userId, UUID postId){
        String sql = "UPDATE posts SET upvotes = upvotes - 1 WHERE id = ?";
        jdbcTemplate.update(sql, postId);
        sql = "DELETE FROM upvotes WHERE postId = ? AND userId = ?";
        jdbcTemplate.update(sql, postId, userId);
    }

    public void deletePostLikes(UUID postId){
        String sql = "DELETE FROM upvotes WHERE postid = ?";
        jdbcTemplate.update(sql, postId);
    }

    public void deleteUserComments(UUID userId){
        String sql = "DELETE FROM comments WHERE userId = ?";
        jdbcTemplate.update(sql, userId);
    }

    public List<String> getAllTags(){
        String sql = "SELECT * FROM tags";
        return jdbcTemplate.query(sql, (resultSet, i) -> resultSet.getString("tag")).stream().distinct().collect(Collectors.toList());
    }

    public List<UUID> getPostLikes(UUID postId){
        String sql = "SELECT * FROM upvotes WHERE postId = ?";
        return jdbcTemplate.query(sql, new Object[]{postId}, (resultSet, i) -> UUID.fromString(resultSet.getString("userId")));
    }
}
