import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { AccountService } from 'src/app/services/account.service';
import { LoadingService } from 'src/app/services/loading.service';
import { AlertService } from 'src/app/services/alert.service';
import { Subscription } from 'rxjs';
import { Login } from 'src/app/model/payload/login';
import { AlertType } from 'src/app/enum/alert-type.enum';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit , OnDestroy{

  private subscriptions: Subscription[] = [];

  constructor(
    private router: Router,
    private accountService: AccountService,
    private loadingService: LoadingService,
    private alertService: AlertService
  ) { }

  ngOnInit() {
    if (this.accountService.isLoggedIn()) {
      if (this.accountService.redierctURL) {
        this.router.navigateByUrl(this.accountService.redierctURL);
      } else {
        this.router.navigate(['/home']);
      }
    } else {
      this.router.navigate(['/login']);
    }
  }

  onLogin(login: Login): void {
    this.loadingService.isLoading.next(true);
    this.subscriptions.push(
      this.accountService.login(login).subscribe(
        (res: any) => {
          const token = res.body.token;
          this.accountService.saveToken(token);
          if (this.accountService.redierctURL) {
            this.router.navigateByUrl(this.accountService.redierctURL);
          } else {
            this.router.navigate(['/home']);
          }
          this.loadingService.isLoading.next(false);
        }, err => {
          console.log(err);
          this.loadingService.isLoading.next(false);
          this.alertService.showAlert(
            'Username or password incorrect. please try again.',
            AlertType.DANGER
          );
        }
      )
    );
  }

  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}
