import { Component, OnInit, OnDestroy } from '@angular/core';
import { Server } from 'src/app/constants/server';
import { Router, ActivatedRoute } from '@angular/router';
import { AccountService } from 'src/app/services/account.service';
import { LoadingService } from 'src/app/services/loading.service';
import { AlertService } from 'src/app/services/alert.service';
import { PostService } from 'src/app/services/post.service';
import { User } from 'src/app/model/user';
import { Post } from 'src/app/model/post';
import { Subscription } from 'rxjs';
import { AlertType } from 'src/app/enum/alert-type.enum';
import { ChangePassword } from 'src/app/model/payload/change-password';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit , OnDestroy {

  private constant: Server = new Server();
  private subscriptions: Subscription [] = [];
  postId: number;
  user: User;
  posts: Post[] = [];
  host: string;
  userHost: string;
  postHost: string;
  username: string;
  post: Post = new Post();
  profilePictureChange: boolean;
  profilePicture: File;

  constructor(
    private router: Router,
    public accountService: AccountService,
    private loadingService: LoadingService,
    private alertService: AlertService,
    private postService: PostService,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.loadingService.isLoading.next(true);
    this.username = this.route.snapshot.paramMap.get('username');
    this.host = this.constant.host;
    this.userHost = this.constant.userPicture;
    this.postHost = this.constant.postPicture;
    this.getUserInfo(this.username);
    this.loadingService.isLoading.next(false);
  }

  getUserInfo(username: string): void {
    this.subscriptions.push(
      this.accountService.getUserInfo(username).subscribe(
        (res: User) => {
          console.log('---------> ' ,  res);
          this.user = res;
          this.getPostsByUsername(username);
        }, err => {
          console.log(err);
          this.user = null;
        }
      )
    );
  }

  getPostsByUsername(username: string): void {
    this.subscriptions.push(
      this.postService.getPostsByUsername().subscribe(
        (res: Post[]) => {
          this.user.posts = res;
        }, err => {
          console.log(err);
          this.user.posts = null;
        }
      )
    );
  }

  onProfilePictureSelected(event: any): void {
    console.log(event);
    this.profilePicture = <File>event.target.files[0];
    console.log(this.profilePicture);
    this.profilePictureChange = true;
  }

  onUpdateUser(updateUser: User): void {
    this.loadingService.isLoading.next(true);
    this.subscriptions.push(
      this.accountService.updateUser(updateUser).subscribe(
        res => {
          console.log(res);
          if (this.profilePictureChange) {
            this.accountService.uploadUserProfilePicture(this.profilePicture).subscribe(
              data => {
                console.log(data);
              }, err => {
                console.log(err);
              }
            )
          }
          this.loadingService.isLoading.next(false);
          this.alertService.showAlert('Profile updated successfully.', AlertType.SUCCESS);
        }, err => {
          console.log(err);
          this.loadingService.isLoading.next(false);
          this.alertService.showAlert('Profile updated failed. Please try again.', AlertType.DANGER);
        }
      )
    );
  }

  onChangePassword(passwordChange: ChangePassword): void {
    const element: HTMLElement = document.getElementById('changePasswordDismiss') as HTMLElement ;
    element.click();
    this.loadingService.isLoading.next(true);
    this.subscriptions.push(
      this.accountService.changePassword(passwordChange).subscribe(
        res => {
          console.log(res);
          this.loadingService.isLoading.next(false);
          this.alertService.showAlert('Password was updated successfully.', AlertType.SUCCESS);
        }, err => {
          console.log(err);
          this.loadingService.isLoading.next(false);
          const errorMessage = err.error;
          this.showError(errorMessage);
        }
      )
    );
  }
  private showError(errorMessage: string): void {
    if (errorMessage === 'PasswordNotMatched') {
      this.alertService.showAlert(
        'Passwords do not match. Please try again.',
        AlertType.DANGER
      );
    } else if (errorMessage === 'IncorrectCurrentPassword') {
      this.alertService.showAlert(
        'The current password is incorrect. Please try again.',
        AlertType.DANGER
      );
    } else {
      this.alertService.showAlert(
        'Password change failed. Please try again.',
        AlertType.DANGER
      );
    }
  }
  seeOnePost(postId): void {
    this.router.navigate(['/post', postId]);
    console.log(postId);
  }
  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}
