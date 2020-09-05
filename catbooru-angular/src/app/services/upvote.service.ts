import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class UpvoteService {
  url:string = 'http://localhost:8080/posts/';

  constructor(private userService:UserService, private http:HttpClient) { }

  getPostUpvotes(postId:string):Observable<string[]>{
      return this.http.get<string[]>(this.url + postId + '/upvotes')
  }

  likePost(postId:string){
    const headerDict = {
      'Authorization': 'Bearer '+this.userService.user.token
    }
    const requestOptions = {                                                                                                                                                                             
      headers: new HttpHeaders(headerDict),
      params: new HttpParams().append('userId', this.userService.user.id)
    };
      return this.http.post(this.url + postId + '/like', requestOptions, requestOptions )
  }

  unlikePost(postId:string){
    const headerDict = {
      'Authorization': 'Bearer '+this.userService.user.token
    }
    const requestOptions = {                                                                                                                                                                             
      headers: new HttpHeaders(headerDict),
      params: new HttpParams().append('userId', this.userService.user.id)
    };
      return this.http.delete(this.url + postId + '/unlike', requestOptions)
  }
}
