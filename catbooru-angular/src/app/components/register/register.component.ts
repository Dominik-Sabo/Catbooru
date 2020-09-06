import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { UserService } from '../../services/user.service'

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  @Output() emitter = new EventEmitter<string>();
  username:string = '';
  password:string = '';
  confirm:string = '';
  wrong:string;
  show:boolean = false;
  constructor(private userService:UserService) { }

  ngOnInit(): void {
  }

  onUsernameKey(event) {this.username = event.target.value; if(event.key === 'Enter') this.onClick();}

  onPasswordKey(event) {this.password = event.target.value; if(event.key === 'Enter') this.onClick();}

  onConfirmKey(event) {this.confirm = event.target.value; if(event.key === 'Enter') this.onClick();}

  onClick(){
    if(this.username.length < 1){ this.wrong = "Username required"; this.show = true;}
    else if(this.password.length < 1){ this.wrong = "Password required"; this.show = true;}
    else if(this.password != this.confirm){ this.wrong = "Password confirmation failed"; this.show = true;}
    else
    this.userService.register(this.username, this.password).subscribe(
      user => {
        this.userService.user = user;
        localStorage.setItem('userdata', JSON.stringify(this.userService.user));
        this.emitter.emit('registered')
      },
      error => {
        this.show = true;
        this.wrong = "Username already taken"
      })
  }

}
