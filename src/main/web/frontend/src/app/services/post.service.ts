import { Comment } from './../model/comment';
import { HttpClient, HttpErrorResponse, HttpResponse, HttpHeaders } from '@angular/common/http';
import { Observable, observable, ObservedValueOf } from 'rxjs';
import { Injectable } from '@angular/core';
import { Server } from '../constants/server';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Post } from '../model/post';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private constant: Server = new Server();
  private host: string = this.constant.host;
  private token: string;
  public loggInUsername: string | null;
  public redierctURL: string;
  private jwtHelper: JwtHelperService = new JwtHelperService();

  constructor(private http: HttpClient) { }

  getPosts(): Observable<any> {
    return this.http.get(`${this.host}/api/post` , {observe: 'response'});
  }

  creatPost(post: Post): Observable<Post> {
    return this.http.post<Post>(`${this.host}/api/post`, post);
  }

  getOneById(id: number): Observable<Post> {
    return this.http.get<Post>(`${this.host}/api/post/${id}`);
  }

  getPostsByUsername(): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.host}/api/post/username`);
  }

  saveComment(comment: Comment): Observable<any> {
    return this.http.post<any>(`${this.host}/api/comments`, comment);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.host}/api/post/${id}`);
  }

  like(id: number): Observable<any> {
    return this.http.post(`${this.host}/api/post/like`, id, {responseType: 'text'});
  }
  unLike(id: number): Observable<any> {
    return this.http.post(`${this.host}/api/post/unlike`, id, {responseType: 'text'});
  }

  uploadPostProfilePicture(picture: File) {
    const fd = new FormData();
    fd.append('image', picture);
    return this.http.post(`${this.host}/api/post/photo-upload` , fd , {responseType: 'text', reportProgress: true, observe: 'events'});
  }
}
