import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { AccountService } from 'src/app/services/account.service';
import { LoadingService } from 'src/app/services/loading.service';
import { AlertService } from 'src/app/services/alert.service';
import { Register } from 'src/app/model/payload/register';
import { HttpErrorResponse } from '@angular/common/http';
import { AlertType } from 'src/app/enum/alert-type.enum';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit , OnDestroy{

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
      this.router.navigate(['/sign-up']);
    }
  }

  onRegister(register: Register): void {
    this.loadingService.isLoading.next(true);
    this.subscriptions.push(
      this.accountService.register(register).subscribe(
        res => {
          this.loadingService.isLoading.next(false);
          this.alertService.showAlert('You have registred successfully. please check your email for account details', AlertType.SUCCESS);
        }, (err: HttpErrorResponse) => {
          this.loadingService.isLoading.next(false);
          const errorMessage = err.error;
          if (errorMessage === 'usernameExists') {
            this.alertService.showAlert('This username already exists, please try with a different username', AlertType.DANGER);
          } else if(errorMessage === 'emailExists') {
            this.alertService.showAlert('This email already exists, please try with a different email', AlertType.DANGER);
          } else {
            this.alertService.showAlert('Something went wrong, Please try again.', AlertType.DANGER);
          }
        }
      )
    );
  }

  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}
