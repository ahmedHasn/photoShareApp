import { Injectable } from '@angular/core';
import { Server } from '../constants/server';
import { JwtHelperService } from '@auth0/angular-jwt';
import { HttpClient, HttpErrorResponse, HttpResponse, HttpHeaders } from '@angular/common/http';
import { Observable, observable } from 'rxjs';
import { Login } from '../model/payload/login';
import { Register } from '../model/payload/register';
import { User } from '../model/user';
import { ChangePassword } from '../model/payload/change-password';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  private constant: Server = new Server();
  private host: string = this.constant.host;
  private token: string;
  public loggInUsername: string;
  public redierctURL: string;
  private jwtHelper: JwtHelperService = new JwtHelperService();
  public googleMapsApiKey = 'AIzaSyCR0p5kA-eTUkpSSFmYLrUoNnmyRruDtTc';
  public googleMapsApiUrl = 'https://maps.googleapis.com/maps/api/geocode/json?latlng=';

  constructor(private http: HttpClient) { }

  login(login: Login): Observable<HttpErrorResponse | HttpResponse<any>> {
    return this.http.post<HttpErrorResponse | HttpResponse<any>>(`${this.host}/login`, login , { observe: 'response' });
  }

  register(register: Register): Observable<HttpErrorResponse | HttpResponse<any>> {
    return this.http.post<HttpErrorResponse | HttpResponse<any>>(`${this.host}/api/register` , register);
  }

  resetPassword(email: string) {
    return this.http.get(`${this.host}/api/reset-password/${email}` , {responseType: 'text'});
  }

  logout(): void{
    this.token = null;
    localStorage.removeItem('token');
  }

  saveToken(token: string): void{
    this.token = token;
    localStorage.setItem('token', 'Bearer ' + token);
  }

  loadToken(): void {
    this.token = localStorage.getItem('token');
  }

  getToken(): string {
    return this.token;
  }

  isLoggedIn(): boolean {
    this.loadToken();
    if (this.token != null && this.token !== '') {
        if (this.jwtHelper.decodeToken(this.token).sub != null || '') {
          if (!this.jwtHelper.isTokenExpired(this.token)) {
            this.loggInUsername = this.jwtHelper.decodeToken(this.token).sub;
            return true;
          }
        }
    } else {
      this.logout();
      return false;
    }
  }

  getUserInfo(username: string): Observable<any>{
    return this.http.get(`${this.host}/api/users/username/${username}`);
  }

  searchUser(username: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.host}/api/users/list/${username}`);
  }

  getLocation(latitude: string, longitude: string): Observable<any> {
    return this.http.get<any>(`${this.googleMapsApiUrl}` + `${latitude},${longitude}&key=${this.googleMapsApiKey}`);
  }

  updateUser(user: User): Observable<User> {
    return this.http.put<User>(`${this.host}/api/users`, user);
  }

  changePassword(changePassword: ChangePassword) {
    return this.http.post(`${this.host}/api/change-password`, changePassword , {responseType: 'text'});
  }

  uploadUserProfilePicture(picture: File) {
    const fd = new FormData();
    fd.append('image', picture);
    return this.http.post(`${this.host}/api/users/photo-upload` , fd , {responseType: 'text'});
  }
}
