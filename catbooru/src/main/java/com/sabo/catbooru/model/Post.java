package com.sabo.catbooru.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

public class Post {

    private UUID id;
    private UUID userId;
    private String filePath;
    private int upvotes;
    private String tags;
    private MultipartFile imageFile;
    private Resource imageResource;
    private Date time;

    public Post(){};

    public Post(UUID userId, @JsonProperty MultipartFile imageFile, @JsonProperty String tags) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.imageFile = imageFile;
        this.tags = tags;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Resource getImageResource() {
        return imageResource;
    }

    public void setImageResource(Resource imageResource) {
        this.imageResource = imageResource;
    }

}
