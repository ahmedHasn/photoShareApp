import { Injectable } from '@angular/core';
import { AccountService } from '../services/account.service';
import { CacheService } from '../services/cache.service';
import { Server } from '../constants/server';
import { HttpClient, HttpErrorResponse, HttpResponse, HttpHeaders, HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable, observable, of } from 'rxjs';
import {tap } from "rxjs/operators";
import { THIS_EXPR } from '@angular/compiler/src/output/output_ast';

@Injectable({
  providedIn: 'root'
})
export class CacheInterceptorService implements HttpInterceptor {

  private constant: Server = new Server();
  private host: string = this.constant.host;

  constructor(private accountService: AccountService, private cacheService: CacheService) { }


  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (req.method !== 'GET') {
      this.cacheService.clearCache();
      return next.handle(req);
    }

    if (req.url.includes(`${this.host}/api/register`)) {
      return next.handle(req);
    }
    if (req.url.includes(`${this.host}/login`)) {
      return next.handle(req);
    }
    if (req.url.includes(`${this.host}/api/reset-password`)) {
      return next.handle(req);
    }
    if (req.url.includes(`${this.host}/api/users/username`)) {
      return next.handle(req);
    }

    const cacheResponse: HttpResponse<any> = this.cacheService.getCache(req.url);

    if (cacheResponse) {
      return of (cacheResponse);
    }

    return next.handle(req).pipe(tap(event => {
      if (event instanceof HttpResponse) {
        this.cacheService.cacheRequest(req.url, event);
      }
    }));
  }
}
