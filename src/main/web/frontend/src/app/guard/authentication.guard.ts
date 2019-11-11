import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Server } from '../constants/server';
import { AccountService } from '../services/account.service';
import { AlertService } from '../services/alert.service';
import { AlertType } from '../enum/alert-type.enum';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationGuard implements CanActivate {

  private constant: Server = new Server();
  private host: string = this.constant.host;

  constructor(private accountService: AccountService, private alertService: AlertService, private router: Router){}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.isLoggedIn(state.url);
  }

  private isLoggedIn(url: string): boolean {
    if (this.accountService.isLoggedIn()) {
      return true;
    }
    this.accountService.redierctURL = url;
    this.router.navigate(['/login']);
    this.alertService.showAlert('You must be logged in to access this page', AlertType.DANGER);
    return false;
  }
}
