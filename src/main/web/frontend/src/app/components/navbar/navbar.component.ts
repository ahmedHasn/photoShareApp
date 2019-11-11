import { Component, OnInit, OnDestroy } from '@angular/core';
import { AccountService } from 'src/app/services/account.service';
import { PostService } from 'src/app/services/post.service';
import { LoadingService } from 'src/app/services/loading.service';
import { AlertService } from 'src/app/services/alert.service';
import { Server } from 'src/app/constants/server';
import { Subscription } from 'rxjs';
import { User } from 'src/app/model/user';
import { Router } from '@angular/router';
import { Post } from 'src/app/model/post';
import { HttpEventType } from '@angular/common/http';
import { AlertType } from 'src/app/enum/alert-type.enum';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit , OnDestroy {

  private constant: Server = new Server();
  private subscription: Subscription[] = [];
  user: User;
  searchedUser: User[];
  host: string;
  userHost: string;
  postHost: string;
  postPicture: File;
  username: string;
  userLoggedIn: boolean;
  showNavbar: boolean;
  showSuccessAlert: boolean;
  photoName: string;
  latitude: any;
  longitude: any;
  location = null;
  progress: number;
  newPostUrl: string;
  clientHost: string;
  postFail: boolean;

  constructor(
    private accountService: AccountService,
    private postService: PostService,
    private loadingService: LoadingService,
    private alertService: AlertService,
    private router: Router
  ) { }

  ngOnInit() {
    this.loadingService.isLoading.next(true);
    this.host = this.constant.host;
    this.clientHost = this.constant.client;
    this.userHost = this.constant.userPicture;
    this.postHost = this.constant.postPicture;
    this.showNavbar = true;
    if (this.accountService.isLoggedIn) {
      this.username = this.accountService.loggInUsername;
      this.getUserInfo(this.username);
      this.getLonAndLat();
      this.loadingService.isLoading.next(false);
    } else {
      this.showNavbar = false;
      this.loadingService.isLoading.next(true);
    }
  }

  getUserInfo(username: string): void {
    this.subscription.push(
      this.accountService.getUserInfo(username).subscribe(
        (res: User) => {
          this.user = res;
          this.userLoggedIn = true;
          this.showNavbar = true;
        }, err => {
          this.userLoggedIn = false;
        }
      )
    );
  }

  onSearchUsers(event) {
    const username = event;
    this.subscription.push(
      this.accountService.searchUser(username).subscribe(
        (res: User[]) => {
          this.searchedUser = res;
        }, err => {
          console.log('----- error ', err);
          return this.searchedUser = [];
        }
      )
    );
  }

  getUserProfile(username: string): void {
    this.router.navigate(['/profile', username]);
  }

  getSearchUserProfile(username: string): void {
    const element: HTMLElement = document.getElementById('closeSearchModel') as HTMLElement ;
    element.click();
    this.router.navigate(['/profile', username]);
    setTimeout(() => {
      location.reload();
    }, 100);
  }

  onFileSelected(event: any): void {
    console.log('file was selected');
    this.postPicture = event.target.files[0];
    this.photoName = this.postPicture.name;
    console.log(this.photoName);
  }

  onNewPost(post: Post): void {
    const element: HTMLElement = document.getElementById('dismissOnSubmitPost') as HTMLElement ;
    element.click();
    this.loadingService.isLoading.next(true);
    this.subscription.push(
      this.postService.creatPost(post).subscribe(
        (res: Post) => {
          console.log(res);
          let postId: number = res.id;
          this.savePicture(this.postPicture);
          this.loadingService.isLoading.next(false);
          this.newPostUrl = `${this.clientHost}/post/${postId}`;
        }, err => {
          console.log(err);
          this.postFail = true;
          this.loadingService.isLoading.next(false);
        }
      )
    );
  }

  savePicture(picture: File): void {
    this.subscription.push(
      this.postService.uploadPostProfilePicture(picture).subscribe(
        res => {
          if (res.type === HttpEventType.UploadProgress) {
            this.progress = (res.loaded / res.total) * 100;
          }else {
            console.log(res);
            this.onNewPostSuccess(8);
          }
        }, err => {
          console.log(err);
        }
      )
    );
  }

  onNewPostSuccess(second: number): void {
    this.showSuccessAlert = true;
    setTimeout(() => {
      this.showSuccessAlert = false;
      this.newPostUrl = null;
    }, second * 1000);
  }

  logOut(): void {
    this.loadingService.isLoading.next(true);
    this.accountService.logout();
    this.router.navigate(['/login']);
    this.loadingService.isLoading.next(false);
    this.alertService.showAlert(
      'You have been successfully logged out',
      AlertType.SUCCESS
    );
  }

  getLonAndLat(): void {
    if ( window.navigator && window.navigator.geolocation ) {
      window.navigator.geolocation.getCurrentPosition(
        position => {
          this.latitude = position.coords.latitude;
          this.longitude = position.coords.longitude;
          this.getUserLocation(this.latitude, this.longitude);
        }, err => {
          switch (err.code) {
            case 1:
              console.log('Permission Location Denied.');
              break;
            case 2:
              console.log('Permission unavailable.');
              break;
            case 3:
              console.log('Timeout.');
              break;
          }
        }
      );
    }
  }

  getUserLocation(latitude, longitude): void {
    this.subscription.push(
      this.accountService.getLocation(latitude, longitude).subscribe(
        (res: any) => {
          // this.location = res.results[3].formatted_address;
        }, err => {
          console.log(err);
        }
      )
    );
  }

  ngOnDestroy() {
    this.subscription.forEach(sub => sub.unsubscribe());
  }

}
