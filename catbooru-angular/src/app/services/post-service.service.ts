import { Injectable } from '@angular/core';
import { Observable } from 'rxjs'
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http'

import { Post } from '../models/Post'
import { User } from '../models/User';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  query:string = '';
  order:string = 'descending';
  sort:string = 'time';
  url:string = 'http://localhost:8080/posts/'

  constructor(private userService:UserService, private http:HttpClient) { }

  getAllPosts():Observable<Post[]>{
    return this.http.get<Post[]>(this.url + 'all', {params: new HttpParams().set('order', 'descending').set('sort', 'time') })
  }

  getPosts():Observable<Post[]>{
    if(this.query==null) this.query='';
    return this.http.get<Post[]>(this.url + 'query', {params: new HttpParams().set('query', this.query).set('order', this.order).set('sort', this.sort) })
  }

  getPost(postId:string):Observable<Post>{
    return this.http.get<Post>(this.url + postId)
  }

  getLikedPosts(userId:string):Observable<Post[]>{
    return this.http.get<Post[]>(this.url + 'liked', {params: new HttpParams().set('userId', userId).set('order', this.order).set('sort', this.sort) })
  }

  getCommentedPosts(userId:string):Observable<Post[]>{
    return this.http.get<Post[]>(this.url + 'commented', {params: new HttpParams().set('userId', userId).set('order', this.order).set('sort', this.sort) })
  }

  addNewPost(user:User, formData:FormData){
    const headerDict = {
      'Authorization': 'Bearer '+user.token
    }
    const requestOptions = {                                                                                                                                                                             
      headers: new HttpHeaders(headerDict),
    };
    return this.http.post(this.url + 'new', formData, requestOptions)
  }

  deletePost(postId:string){
    const headerDict = {
      'Authorization': 'Bearer '+this.userService.user.token
    }
    const requestOptions = {                                                                                                                                                                             
      headers: new HttpHeaders(headerDict),
    };
    return this.http.delete(this.url + postId + '/delete', requestOptions)
  }
}