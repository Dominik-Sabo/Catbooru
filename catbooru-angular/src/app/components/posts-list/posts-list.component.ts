import { Component, OnInit } from '@angular/core';
import { PostService } from '../../services/post-service.service'
import { Post } from '../../models/Post'
import { Router, ActivatedRoute } from '@angular/router';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-posts-list',
  templateUrl: './posts-list.component.html',
  styleUrls: ['./posts-list.component.css']
})
export class PostsListComponent implements OnInit {
  posts:Post[];
  notfound:string = ''

  constructor(private postService:PostService, private userService:UserService, private router:Router, private route:ActivatedRoute) { }

  ngOnInit(): void {
    if(this.router.url.includes('/liked')){
      if(this.userService.user == null) this.router.navigate(['login']);
      else this.postService.getLikedPosts(this.userService.user.id).subscribe(posts => {
        this.posts = posts;
        if(this.posts == null) this.notfound = "No posts found";
      });
    }
    else if(this.router.url.includes('/commented')){
      if(this.userService.user == null) this.router.navigate(['login']);
      else this.postService.getCommentedPosts(this.userService.user.id).subscribe(posts => {
        this.posts = posts;
        if(this.posts == null) this.notfound = "No posts found";
      });
    }
    else if(this.router.url.includes('/all')){
      this.router.navigate(['posts']);
    }
    else{
      this.route.params.subscribe(params => {
        this.postService.query = params['query'];  
        this.postService.getPosts().subscribe(posts => {
          this.posts = posts;
          if(this.posts == null) this.notfound = "No posts found";
        });
      })
    this.postService.query = '';
    }
  }
  


  onClick(post:Post){
    this.navigate(post.id)
  }

  navigate(id:string){
    this.router.navigate(['posts/post', id])
  }
}
