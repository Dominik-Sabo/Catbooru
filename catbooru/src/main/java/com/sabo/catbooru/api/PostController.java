package com.sabo.catbooru.api;

import com.sabo.catbooru.model.Post;
import com.sabo.catbooru.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/new/{userId}")
    public ResponseEntity<byte[]> createNewPost(@PathVariable("userId") UUID userId, @RequestParam MultipartFile imageFile, @RequestParam String tags) throws Exception {
        try{
            postService.createNewPost(new Post(userId, imageFile, tags));
        }
        catch (Exception e){
            throw e;
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePostById(@PathVariable UUID id){
        postService.deletePostById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/addtags/{id}")
    public ResponseEntity<?> addTag(@PathVariable UUID id, @RequestParam String tags){
        postService.addTags(id, tags);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deletetag/{id}")
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

    @GetMapping("/post/{id}")
    public ResponseEntity<Post> getPost(@PathVariable UUID id){
        Post post = postService.getPost(id);
        return ResponseEntity.ok().body(post);
    }

    @PostMapping("/like/{postId}")
    public ResponseEntity<?> likePost(@PathVariable UUID postId, @RequestParam UUID userId){
        postService.likePost(userId, postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/unlike/{postId}")
    public ResponseEntity<?> unlikePost(@PathVariable UUID postId, @RequestParam UUID userId){
        postService.unlikePost(userId, postId);
        return ResponseEntity.ok().build();
    }
    
}
