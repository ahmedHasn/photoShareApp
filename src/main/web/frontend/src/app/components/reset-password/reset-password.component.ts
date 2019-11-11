import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { AccountService } from 'src/app/services/account.service';
import { LoadingService } from 'src/app/services/loading.service';
import { AlertService } from 'src/app/services/alert.service';
import { AlertType } from 'src/app/enum/alert-type.enum';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit , OnDestroy{

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
      } else{
        this.router.navigateByUrl('/home');
      }
    } else {
      this.router.navigateByUrl('/reset-password');
    }
  }

  onResetpassword(form): void {
    this.loadingService.isLoading.next(true);
    this.subscriptions.push(
      this.accountService.resetPassword(form).subscribe(
        res => {
          this.loadingService.isLoading.next(false);
          this.alertService.showAlert('A new password has been sent to your email.', AlertType.SUCCESS);
        }, (err: HttpErrorResponse) => {
          this.loadingService.isLoading.next(false);
          const errorMessage = err.error;
          if (errorMessage === 'User Not found') {
            this.alertService.showAlert('TThere is no account for this email. Please verify the  email.', AlertType.DANGER);
          }
          if (errorMessage !== 'User Not found') {
            this.alertService.showAlert('Some error occured. lease try again', AlertType.DANGER);
          }
        }
      )
    );
  }

  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}
