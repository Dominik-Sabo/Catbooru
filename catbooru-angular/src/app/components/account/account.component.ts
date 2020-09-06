import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { PostService } from 'src/app/services/post-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit {
  @Output() emitter = new EventEmitter<string>();
  changeusername:string;
  password:string = '';
  username:string= '';
  confirm:string= '';
  show:boolean = false;
  error:string;

  constructor(private userService:UserService, private postService:PostService, private router:Router) { }

  onUsernameKey(event) {this.changeusername = event.target.value; if(event.key === 'Enter') this.onChangeClick();}

  onPasswordKey(event) {this.password = event.target.value; if(event.key === 'Enter') this.onChangeClick();}

  onConfirmKey(event) {this.confirm = event.target.value; if(event.key === 'Enter') this.onChangeClick();}

  ngOnInit(): void {
    if(this.userService.user == null){
      this.router.navigate(['login']);
      this.emitter.emit('expired');
    } 
    this.username = this.userService.user.username
  }

  onLikedClick(){
    this.router.navigate(['posts/liked']);
  }

  onCommentedClick(){
    this.router.navigate(['posts/commented']);
  }

  onPostsClick(){
    this.postService.query=this.userService.user.username;
    this.router.navigate(['posts', this.userService.user.username]);
  }

  onChangeClick(){
    if(this.username == '' || this.password == ''){
      this.error = 'Both fields must be filled'
      this.show = true;
    } 
    else if(this.password != this.confirm){ this.error = "Password confirmation failed"; this.show = true;}
    else{
      this.userService.changeUsernamePassword(this.changeusername, this.password).subscribe(
        user => {
          let id = this.userService.user.id
          this.userService.user = user;
          this.userService.user.id = id;
          localStorage.setItem('userdata', JSON.stringify(this.userService.user));
          this.username = this.userService.user.username
          this.changeusername = '';
          this.password = '';
          this.confirm = '';
          this.show = false;
        },
        error => {          
          this.error = 'Username already taken';
          this.show = true;
          if(error.error == null){
            this.userService.logout()
            this.router.navigate(["login"]);
          }
        })
    }
  }

  onLogoutClick(){
    this.userService.logout();
    this.emitter.emit('loggedOut');
  }

  onDeleteClick(){
    if(confirm("Deleting your account will also delete all your posts and comments. Are you sure?")){
      this.userService.delete().subscribe(onDelete =>{
        this.userService.logout();
        this.emitter.emit('accountDeleted');
      },
      error =>{
        if(error.error == null){
          this.userService.logout()
          this.router.navigate(["login"]);
        }
      })
    }
  }
}
