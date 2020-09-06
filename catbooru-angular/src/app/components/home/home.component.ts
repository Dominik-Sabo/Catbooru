import { Component, OnInit } from '@angular/core';
import { Router, } from '@angular/router';
import { PostService } from 'src/app/services/post-service.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  query: string;
  order: string[];
  login:string = 'Login';
  message:string = ''

  constructor(private router:Router, private postService:PostService, private userService:UserService ) { this.router.routeReuseStrategy.shouldReuseRoute = () => false;}

  ngOnInit(): void {
    if(this.userService.user != null) this.login = 'Account';
  }

  onKey(event) {
    this.query = event.target.value;
    if(event.key === 'Enter') this.onSearchClick();
  }

  onSelect(event) {this.order = event.target.value.split(' '); this.postService.sort = this.order[0]; this.postService.order = this.order[1]}

  onPostsClick(){
    this.router.navigate(['posts/all']);
    this.message = '';
  }

  onLoginClick(){
    if(this.login == 'Login') this.router.navigate(['login']);
    else this.router.navigate(['account'])
    this.message = '';
  }

  onTagsClick(){
    this.router.navigate(['tags']);
    this.message = '';
  }

  onNewClick(){
    this.router.navigate(['new']);
    this.message = '';
  }

  onSearchClick(){
    if(this.query == null) this.query = 'all';
    this.postService.query = this.query;
    this.navigate(this.query)
    this.message = '';
  }

  navigate(query:string){
    this.router.navigate(['posts', query]);
  }

  onActivate(elementRef) {
    elementRef.emitter.subscribe(event => {
      switch(event){
        case 'loggedIn': this.login = 'Account'; this.router.navigate(['account']); break;
        case 'registered':  this.login = 'Account'; this.router.navigate(['account']); break;
        case 'deleted': this.message = 'Post deleted'; this.router.navigate(['']); break;
        case 'loggedOut': this.login = 'Login'; this.message = 'See you later'; this.router.navigate(['']); break;
        case 'accountDeleted': this.login = 'Login'; this.message = 'Fare thee well'; this.router.navigate(['']); break;
        case 'expired': this.login = 'Login'; break;
        case 'uploaded': this.message = 'Post uploaded'; this.router.navigate(['']); break;
        default: this.router.navigate([event]);
      }
    });
}

  home(){
    this.router.navigate([''])
    this.message = '';
  }


  
}
