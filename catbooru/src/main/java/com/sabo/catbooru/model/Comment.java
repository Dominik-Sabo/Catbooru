package com.sabo.catbooru.model;

import java.util.Date;
import java.util.UUID;

public class Comment {
    private UUID id;
    private UUID postId;
    private UUID userId;
    private String username;
    private String commentText;
    private Date time;

    public Comment(){}

    public Comment(UUID postId, UUID userId, String commentText) {
        this.id = UUID.randomUUID();
        this.postId = postId;
        this.userId = userId;
        this.commentText = commentText;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPostId() {
        return postId;
    }

    public void setPostId(UUID postId) {
        this.postId = postId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
