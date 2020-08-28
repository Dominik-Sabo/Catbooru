package com.sabo.catbooru.service;

import com.sabo.catbooru.dao.PostDataAccessService;
import com.sabo.catbooru.model.Post;
import com.sabo.catbooru.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@Service
public class PostService {

    @Autowired
    private FileStorageService storageService;

    @Autowired UserService userService;

    @Autowired
    private PostDataAccessService postDataAccessService;

    public void createNewPost(Post post) throws Exception{
        storageService.save(post.getImageFile());
        post.setFilePath(post.getImageFile().getOriginalFilename());
        postDataAccessService.createNewPost(post);
        if(post.getTags().isBlank()) return;
        postDataAccessService.addPostTags(post.getId(), listToArray(removeDuplicateTags(post.getTags())));
    }

    public void deletePostById(UUID id){
        postDataAccessService.deletePostTags(id);
        postDataAccessService.deletePostById(id);
    }

    public void addTags(UUID id, String tagString){
        postDataAccessService.addPostTags(id, listToArray(removeTableDuplicateTags(tagString, id)));
    }

    public void deleteTag(UUID id, String tag){
        postDataAccessService.deleteTag(id, tag);
    }

    public List<Post> getAllPosts(String order, String sort){
        List<Post> posts = postDataAccessService.getAllPosts();
        if(posts.isEmpty()) return null;

        for(Post post : posts){
            post.setImageResource(storageService.load(post.getFilePath()));
        }

        return sortPosts(posts, order, sort);
    }



    public List<Post> filterPosts(String query, String order, String sort){

        ArrayList<String> listQuery = removeDuplicateTags(query);
        boolean userInQueryFlag = false;
        User user = null;
        List<Post> postsByUser = new ArrayList<Post>();
        List<Post> postsByTags;
        List<Post> posts = new ArrayList<Post>();

        for(String potentialUsername : listQuery){
             user = userService.getUserByUsername(potentialUsername);
             if(user != null){
                 postsByUser = getUserPosts(user.getId());
                 listQuery.remove(potentialUsername);
                 userInQueryFlag = true;
                 break;
             }
        }

        if(!listQuery.isEmpty()){
            postsByTags = postDataAccessService.filterPosts(listToArray(listQuery));
            if(!userInQueryFlag){
                posts = postsByTags;
            }
            else{
                for(Post postUser : postsByUser){
                    if(posts.contains(postUser)) continue;
                    for(Post postTag : postsByTags){
                        if(postUser.getFilePath().equals(postTag.getFilePath())) posts.add(postUser);
                    }
                }
            }
        }
        else posts = postsByUser;

        if(posts.isEmpty()) return null;

        for(Post post : posts){
            post.setImageResource(storageService.load(post.getFilePath()));
        }
        return sortPosts(posts, order, sort);
    }

    public void deleteUserPosts(UUID userId){
        for(Post post : getUserPosts(userId)){
            deletePostById(post.getId());
        }
    }

    public List<Post> getUserPosts(UUID userId){
        return postDataAccessService.getUserPosts(userId);
    }

    public List<Post> getUserPosts(UUID userId, String order, String sort){
        List<Post> posts =  postDataAccessService.getUserPosts(userId);

        for(Post post : posts){
            post.setImageResource(storageService.load(post.getFilePath()));
        }

        return sortPosts(posts, order, sort);
    }

    public Post getPost(UUID id){
        Post post = postDataAccessService.getPost(id).orElse(null);
        if(post == null) return null;
        post.setImageResource(storageService.load(post.getFilePath()));
        return post;
    }

    public void likePost(UUID userId, UUID postId){
        postDataAccessService.likePost(userId, postId);
    }

    public void unlikePost(UUID userId, UUID postId){
        postDataAccessService.unlikePost(userId, postId);
    }

    private List<Post> sortPosts(List<Post> posts, String order, String sort) {
        Comparator<Post> comparator = null;

        switch(sort){
            case "upvotes": comparator = Comparator.comparing(Post::getUpvotes); break;
            default: case "time": comparator = Comparator.comparing(Post::getTime);
        }

        if(order.equals("ascending")) posts.sort(comparator);
        else posts.sort(comparator.reversed());

        return posts;
    }

    private ArrayList<String> removeDuplicateTags(String tagString){

        String[] tags = tagString.split(" ");
        ArrayList<String> tagList = new ArrayList<>(Arrays.asList(tags));

        ArrayList<String> duplicateList = new ArrayList<String>();
        for(String tag : tagList){
            if(!duplicateList.contains(tag)) duplicateList.add(tag);
        }

        return duplicateList;
    }

    private ArrayList<String> removeTableDuplicateTags(String tagString, UUID id){

        ArrayList<String> duplicateList = removeDuplicateTags(tagString);

        List<Object> postTags = postDataAccessService.getPostTags(id);
        for(Object tag : postTags){
            duplicateList.remove(tag.toString());
        }
        return duplicateList;
    }
    private String[] listToArray(ArrayList<String> tagList) {
        String[] tagArray = new String[tagList.size()];
        for (int i = 0; i < tagList.size(); i++) {
            tagArray[i] = tagList.get(i);
        }
        return tagArray;
    }

}
