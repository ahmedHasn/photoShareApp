import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Server } from '../constants/server';
import { AccountService } from '../services/account.service';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptor implements HttpInterceptor {

  private constant: Server = new Server();
  private host: string = this.constant.host;

  constructor(private accountService: AccountService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    if (req.url.includes(`${this.host}/api/register`)) {
      return next.handle(req);
    }
    if (req.url.includes(`${this.host}/login`)) {
      return next.handle(req);
    }
    if (req.url.includes(`${this.host}/api/reset-password`)) {
      return next.handle(req);
    }
    if (req.url.includes('https://maps.googleapis.com/')) {
      return next.handle(req);
    }
    this.accountService.loadToken();
    const token = this.accountService.getToken();
    const request = req.clone({setHeaders: {Authorization: token}});
    return next.handle(request);
  }
}
