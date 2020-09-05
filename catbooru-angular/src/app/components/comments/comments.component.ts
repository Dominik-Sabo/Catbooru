import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';
import { Comment } from '../../models/Comment'
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.css']
})
export class CommentsComponent implements OnInit {
  @Input() comment: Comment;
  @Output() emitter = new EventEmitter<string>();
  @Output() delete = new EventEmitter<Comment>();
  uploaded:boolean = false;
  loggedIn:boolean = false;

  constructor(private userService:UserService) { }

  ngOnInit(): void {
    if(this.userService.user != null && this.userService.user.username == this.comment.username) this.uploaded=true;
  }

  onClick(username:string){
    this.emitter.emit(username);
  }

  onDeleteClick(comment:Comment){
    if(confirm("Are you sure you want to delete this comment?")) this.delete.emit(comment);
  }

}
