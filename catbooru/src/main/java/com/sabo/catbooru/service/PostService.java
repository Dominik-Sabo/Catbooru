package com.sabo.catbooru.service;

import com.sabo.catbooru.dao.PostDataAccessService;
import com.sabo.catbooru.dao.UserDataAccessService;
import com.sabo.catbooru.model.Comment;
import com.sabo.catbooru.model.Post;
import com.sabo.catbooru.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class PostService {

    @Autowired
    private FileStorageService storageService;

    @Autowired UserService userService;

    @Autowired
    private PostDataAccessService postDataAccessService;

    @Autowired
    private UserDataAccessService userDataAccessService;

    public void createNewPost(Post post) throws Exception{
        String filename = UUID.randomUUID().toString();
        if(post.getImageFile().getOriginalFilename().toLowerCase().endsWith(".jpg")) filename = filename + ".jpg";
        else if(post.getImageFile().getOriginalFilename().toLowerCase().endsWith(".jpeg")) filename = filename + ".jpeg";
        else if(post.getImageFile().getOriginalFilename().toLowerCase().endsWith(".png")) filename = filename + ".png";
        else if(post.getImageFile().getOriginalFilename().toLowerCase().endsWith(".gif")) filename = filename + ".gif";
        else throw new Exception();
        storageService.save(post.getImageFile(), filename);
        post.setFilePath(filename);
        postDataAccessService.createNewPost(post);
        if(post.getTags().isBlank()) return;
        postDataAccessService.addPostTags(post.getId(), listToArray(removeDuplicateTags(post.getTags())));
    }

    public void deletePostById(UUID id){
        postDataAccessService.deletePostTags(id);
        postDataAccessService.deletePostComments(id);
        postDataAccessService.deletePostLikes(id);
        postDataAccessService.deletePostById(id);
    }

    public void deleteUserComments(UUID id){
        postDataAccessService.deleteUserComments(id);
    }

    public String addTags(UUID id, String tagString){
        ArrayList<String> tags = removeTableDuplicateTags(tagString, id);
        if(tags.isEmpty()) return null;
        postDataAccessService.addPostTags(id, listToArray(tags));
        return tags.get(0);
    }

    public void deleteTag(UUID id, String tag){
        postDataAccessService.deleteTag(id, tag);
    }

    public List<Post> getAllPosts(String order, String sort){
        List<Post> posts = postDataAccessService.getAllPosts();
        if(posts.isEmpty()) return null;

        for(Post post : posts){
            post.setFilePath(storageService.load(post.getFilePath()));
        }

        return sortPosts(posts, order, sort);
    }

    public List<String> getPostTags(UUID postId){
        List<String> tags = postDataAccessService.getPostTags(postId);
        Collections.sort(tags);
        return tags;
    }



    public List<Post> filterPosts(String query, String order, String sort){

        if(query.isBlank()) return getAllPosts(order, sort);

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
            post.setFilePath(storageService.load(post.getFilePath()));
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

        if(posts.isEmpty()) return null;

        for(Post post : posts){
            post.setFilePath(storageService.load(post.getFilePath()));
        }

        return sortPosts(posts, order, sort);
    }

    public List<Post> getLikedPosts(UUID userId, String order, String sort){

        List<Post> posts =  postDataAccessService.getLikedPosts(userId);

        if(posts.isEmpty()) return null;

        for(Post post : posts){
            post.setFilePath(storageService.load(post.getFilePath()));
        }

        return sortPosts(posts, order, sort);
    }

    public List<Post> getCommentedOnPosts(UUID userId, String order, String sort){

        List<Post> posts =  postDataAccessService.getCommentedOnPosts(userId);

        if(posts.isEmpty()) return null;

        for(Post post : posts){
            post.setFilePath(storageService.load(post.getFilePath()));
        }

        return sortPosts(posts, order, sort);
    }

    public Post getPost(UUID id){
        Post post = postDataAccessService.getPost(id).orElse(null);
        if(post == null) return null;
        post.setFilePath(storageService.load(post.getFilePath()));
        post.setUsername(userDataAccessService.getUsernameById(post.getUserId()));
        return post;
    }

    public List<String> getAllTags(){
        List<String> tags = postDataAccessService.getAllTags();
        Collections.sort(tags);
        return tags;
    }

    public void likePost(UUID userId, UUID postId){
        postDataAccessService.likePost(userId, postId);
    }

    public void unlikePost(UUID userId, UUID postId){
        postDataAccessService.unlikePost(userId, postId);
    }

    public void commentOnPost(Comment comment){
        postDataAccessService.commentOnPost(comment);
    }

    public void deleteCommentById(UUID commentId){
        postDataAccessService.deleteCommentById(commentId);
    }

    public List<Comment> getPostComments(UUID postId){
        List<Comment> comments = postDataAccessService.getPostComments(postId);
        for(Comment comment : comments){
            comment.setUsername(userDataAccessService.getUsernameById(comment.getUserId()));
        }
        comments.sort(Comparator.comparing(Comment::getTime).reversed());
        return comments;
    }

    public List<UUID> getPostLikes(UUID postId){
        return postDataAccessService.getPostLikes(postId);
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

        List<String> postTags = postDataAccessService.getPostTags(id);
        for(String tag : postTags){
            duplicateList.remove(tag);
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
