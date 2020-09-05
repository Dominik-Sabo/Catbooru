import { Injectable } from '@angular/core';
import { Observable } from 'rxjs'
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http'
import { Comment } from '../models/Comment'
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class CommentsService {
  url:string = 'http://localhost:8080/posts/';

  constructor(private userService:UserService, private http:HttpClient) { }

  getPostComments(postId:string):Observable<Comment[]>{
    return this.http.get<Comment[]>(this.url+postId+'/comments')
  }

  addComment(postId:string, commentText:string):Observable<string>{
    const headerDict = {
      'Authorization': 'Bearer '+this.userService.user.token
    }
    const requestOptions = {                                                                                                                                                                             
      headers: new HttpHeaders(headerDict),
      params: new HttpParams().append('userId', this.userService.user.id).append('commentText', commentText),
    };
    return this.http.post<string>(this.url+postId+'/addcomment', requestOptions, requestOptions)
  }

  deleteComment(commentId:string){
    const headerDict = {
      'Authorization': 'Bearer '+this.userService.user.token
    }
    const requestOptions = {                                                                                                                                                                             
      headers: new HttpHeaders(headerDict),
      params: new HttpParams().append('commentId', commentId)
    };
    return this.http.delete(this.url+'deletecomment', requestOptions)
  }
}
