import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  @Output() emitter = new EventEmitter<string>();
  username:string;
  password:string;
  show:boolean = false;

  constructor(private userService:UserService) { }

  ngOnInit(): void {}

  onUsernameKey(event) {this.username = event.target.value; if(event.key === 'Enter') this.onClick();}

  onPasswordKey(event) {this.password = event.target.value; if(event.key === 'Enter') this.onClick();}

  onClick(){
    this.userService.login(this.username, this.password).subscribe(
      user => {
        this.userService.user = user;
        localStorage.setItem('userdata', JSON.stringify(this.userService.user));
        this.emitter.emit('loggedIn')
      },
      error => {
        this.show = true;
      })
  }

  onSignupClick(){
    this.emitter.emit('register');
  }

}
