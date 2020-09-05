import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TagsService } from '../../services/tags.service'
import { CommentsService } from '../../services/comments.service'
import { Comment } from '../../models/Comment'
import { Post } from 'src/app/models/Post';
import { PostService } from 'src/app/services/post-service.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {
  @Output() emitter = new EventEmitter<string>();
  postId:string;
  tags:string[];
  comments:Comment[];
  post:Post;
  tag:string = '';
  comment:string = '';
  loggedIn:boolean = false;
  noComments:boolean = false;

  constructor(private userService:UserService, private tagsService:TagsService, private commentsService:CommentsService, private postService:PostService, private route:ActivatedRoute, private router:Router) { }

  ngOnInit(): void {
    if(this.userService.user != null) this.loggedIn = true;
    
    this.route.params.subscribe(params => {
      this.postId = params['id'];
      this.postService.getPost(this.postId).subscribe(post => {
        this.post = post;
      });
      this.tagsService.getPostTags(this.postId).subscribe(tags => {
        this.tags = tags;
      });
      this.commentsService.getPostComments(this.postId).subscribe(comments => {
        this.comments = comments;
        if(this.comments.length<1) this.noComments = true;
      });
    })
  }

  onTagKey(event) {this.tag = event.target.value; if(event.key === 'Enter') this.onAddTagClick()}

  onCommentKey(event) {this.comment = event.target.value; if(event.key === 'Enter') this.onAddCommentClick()}

  onClick(tag:string){
    this.postService.query=tag;
    this.router.navigate(['posts', tag]);
  }

  onDeleteTagClick(tag:string){
    this.tagsService.deleteTag(this.postId, tag).subscribe(
      deletion => {
      this.tags.splice(this.tags.indexOf(tag), 1)
    },
      error => {
        if(error.error == null){
          this.userService.logout()
          this.router.navigate(["login"]);
        }
      });
  }

  onDeletePostClick(postId:string){
    this.postService.deletePost(postId).subscribe(
      deleted =>{
        this.emitter.emit('deleted');
      },
      error =>{
        if(error.error == null){
          this.userService.logout()
          this.router.navigate(["login"]);
        }
      });
  }

  onDeleteCommentClick(comment:Comment){
    this.commentsService.deleteComment(comment.id).subscribe(
      deletion =>{

       this.comments.splice(this.comments.indexOf(comment), 1)
      },
      error => {
        if(error.error == null){
          this.userService.logout()
          this.router.navigate(["login"]);
        }
      }); 
  }

  onAddTagClick(){
    if (this.tag != '') this.tagsService.addTag(this.postId, this.tag.split(' ')[0]).subscribe(tag => {this.tag = '';},
    error => {
      if(error.error == null){
        this.userService.logout()
        this.router.navigate(["login"]);
      }
      else{
        this.tags.push(this.tag.split(' ')[0]);
        this.tag = '';
      }
    }) 
  }

  onAddCommentClick(){
    if(this.comment == '') return;
    console.log(this.comment);
    this.commentsService.addComment(this.postId, this.comment).subscribe(
      success => {
        let comment = new Comment();
        comment.id = success;
        comment.username = this.userService.user.username;
        comment.commentText = this.comment;
        this.comments.unshift(comment);
        this.comment = '';
        this.noComments = false;
    },
    error=>{
      if(error.error == null){
        this.userService.logout()
        this.router.navigate(["login"]);
      }
    })   
  }

  
}
