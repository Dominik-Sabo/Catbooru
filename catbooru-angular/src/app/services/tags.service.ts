import { Injectable } from '@angular/core';
import { Observable } from 'rxjs'
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http'
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class TagsService {
  url:string = 'http://localhost:8080/posts/';

  constructor(private userService:UserService, private http:HttpClient) { }

  getPostTags(postId:string):Observable<string[]>{
    return this.http.get<string[]>(this.url+postId+'/tags')
  }

  getTags():Observable<string[]>{
    return this.http.get<string[]>(this.url+'tags')
  }

  deleteTag(postId:string, tag:string){
    const headerDict = {
      'Authorization': 'Bearer '+this.userService.user.token
    }
    const requestOptions = {                                                                                                                                                                             
      headers: new HttpHeaders(headerDict),
      params: new HttpParams().append('tag', tag)
    };
    return this.http.delete(this.url + postId + '/deletetag', requestOptions)
  }

  addTag(postId:string, tag:string):Observable<string>{
    const headerDict = {
      'Authorization': 'Bearer '+this.userService.user.token
    }
    const requestOptions = {                                                                                                                                                                             
      headers: new HttpHeaders(headerDict),
      params: new HttpParams().append('tags', tag),
    };
    return this.http.post<string>(this.url + postId + '/addtags', requestOptions, requestOptions)
  }
}
