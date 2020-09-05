import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Post } from 'src/app/models/Post';
import { UserService } from 'src/app/services/user.service';
import { UpvoteService } from 'src/app/services/upvote.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-post-data',
  templateUrl: './post-data.component.html',
  styleUrls: ['./post-data.component.css']
})
export class PostDataComponent implements OnInit {
  @Output() emitter = new EventEmitter<string>();
  @Output() delete = new EventEmitter<string>();
  @Input() post: Post;
  upvotes:string[];
  gotLikes:boolean = false;
  liked:boolean = false;
  uploaded:boolean = false;
  
  constructor(private upvoteService:UpvoteService, private userService:UserService, private router:Router) {}

  ngOnInit(): void {}

  ngAfterContentChecked(){
    if(this.post != undefined){
      if(this.userService.user != undefined){
        if (this.userService.user.username == this.post.username) this.uploaded = true;
        if (!this.gotLikes){
            this.upvoteService.getPostUpvotes(this.post.id).subscribe(upvotes=>{
            this.upvotes = upvotes;
            this.liked = this.upvotes.includes(this.userService.user.id);
          })
          this.gotLikes = true
        }
      } 
    }
  }

  onUpvoteClick(){
    if(this.userService.user == undefined) this.router.navigate(['login'])
    else{
        if(this.liked) this.upvoteService.unlikePost(this.post.id).subscribe(unlike => {
          this.post.upvotes--
          this.liked = !this.liked;
        },
        error => {
          if(error.error == null){
            this.userService.logout()
            this.router.navigate(["login"]);
          }
        });
      else this.upvoteService.likePost(this.post.id).subscribe(like => {
        this.post.upvotes++
        this.liked = !this.liked;
      },
      error => {
        if(error.error == null){
          this.userService.logout()
          this.router.navigate(["login"]);
        }
      });
    }
  }

  onClick(username:string){
    this.emitter.emit(username);
  }

  onDeleteClick(username:string){
    if(confirm("Are you sure?")) this.delete.emit(this.post.id);
  }

}
