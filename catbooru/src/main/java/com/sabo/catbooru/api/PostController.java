package com.sabo.catbooru.api;

import com.sabo.catbooru.model.Comment;
import com.sabo.catbooru.model.Post;
import com.sabo.catbooru.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/new")
    public ResponseEntity<byte[]> createNewPost(@RequestParam("userId") UUID userId, @RequestParam MultipartFile imageFile, @RequestParam String tags) throws Exception {
        try{
            postService.createNewPost(new Post(userId, imageFile, tags));
        }
        catch (Exception e){
            throw e;
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deletePostById(@PathVariable UUID id){
        postService.deletePostById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/addtags")
    public ResponseEntity<String> addTag(@PathVariable UUID id, @RequestParam String tags){
        tags = postService.addTags(id, tags);
        return ResponseEntity.ok().body(tags);
    }

    @DeleteMapping("/{id}/deletetag")
    public ResponseEntity<?> deleteTag(@PathVariable UUID id, @RequestParam String tag){
        postService.deleteTag(id, tag);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Post>> getAllPosts(@RequestParam String order, @RequestParam String sort){
        List<Post> posts = postService.getAllPosts(order, sort);
        return ResponseEntity.ok().body(posts);
    }

    @GetMapping("/query")
    public ResponseEntity<List<Post>> filterPosts(@RequestParam String query, @RequestParam String order, @RequestParam String sort){
        List<Post> posts = postService.filterPosts(query, order, sort);
        return ResponseEntity.ok().body(posts);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Post>> getUserPosts(@RequestParam UUID id, @RequestParam String order, @RequestParam String sort){
        List<Post> posts = postService.getUserPosts(id, order, sort);
        return ResponseEntity.ok().body(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable UUID id){
        Post post = postService.getPost(id);
        return ResponseEntity.ok().body(post);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable UUID postId, @RequestParam UUID userId){
        postService.likePost(userId, postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}/unlike")
    public ResponseEntity<?> unlikePost(@PathVariable UUID postId, @RequestParam UUID userId){
        postService.unlikePost(userId, postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/liked")
    public ResponseEntity<List<Post>> getLikedPosts(@RequestParam UUID userId, @RequestParam String order, @RequestParam String sort){
        List<Post> posts = postService.getLikedPosts(userId, order, sort);
        return ResponseEntity.ok().body(posts);
    }

    @PostMapping("/{postId}/addcomment")
    public ResponseEntity<UUID> commentOnPost(@PathVariable("postId") UUID postId, @RequestParam UUID userId, @RequestParam String commentText){
        Comment comment = new Comment(postId, userId, commentText);
        postService.commentOnPost(comment);
        return ResponseEntity.ok().body(comment.getId());
    }

    @DeleteMapping("/deletecomment")
    public ResponseEntity<?> deleteCommentById(@RequestParam UUID commentId){
        postService.deleteCommentById(commentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<Comment>> getPostComments(@PathVariable("postId") UUID postId){
        List<Comment> comments = postService.getPostComments(postId);
        return ResponseEntity.ok().body(comments);
    }

    @GetMapping("/commented")
    public ResponseEntity<List<Post>> getCommentedOnPosts(@RequestParam UUID userId, @RequestParam String order, @RequestParam String sort) {
        List<Post> posts = postService.getCommentedOnPosts(userId, order, sort);
        return ResponseEntity.ok().body(posts);
    }

    @GetMapping("/{postId}/tags")
    public ResponseEntity<List<String>> getPostTags(@PathVariable("postId") UUID postId) {
        List<String> tags = postService.getPostTags(postId);
        return ResponseEntity.ok().body(tags);
    }

    @GetMapping("/tags")
    public ResponseEntity<List<String>> getAllTags(){
        List<String> tags = postService.getAllTags();
        return ResponseEntity.ok().body(tags);
    }

    @GetMapping("/{postId}/upvotes")
    public ResponseEntity<List<UUID>> getPostLikes(@PathVariable UUID postId){
        List<UUID> likes = postService.getPostLikes(postId);
        return ResponseEntity.ok().body(likes);
    }

}
