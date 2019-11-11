import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { Server } from 'src/app/constants/server';
import { User } from 'src/app/model/user';
import { Post } from 'src/app/model/post';
import { Router } from '@angular/router';
import { AccountService } from 'src/app/services/account.service';
import { LoadingService } from 'src/app/services/loading.service';
import { AlertService } from 'src/app/services/alert.service';
import { PostService } from 'src/app/services/post.service';
import { AlertType } from 'src/app/enum/alert-type.enum';
import { SocketService } from 'src/app/services/socket.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy {
  private constant: Server = new Server();
  private subscriptions: Subscription[] = [];
  user: User;
  posts: Post[] = [];
  host: string;
  userHost: string;
  postHost: string;

  constructor(
    private router: Router,
    private accountService: AccountService,
    private loadingService: LoadingService,
    private alertService: AlertService,
    private postService: PostService,
    private websocket: SocketService
  ) { }

  ngOnInit() {
    this.loadingService.isLoading.next(true);
    this.getUserInfo(this.accountService.loggInUsername);
    this.getPosts();
    this.host = this.constant.host;
    this.userHost = this.constant.userPicture;
    this.postHost = this.constant.postPicture;
    // this.initializeWebSocketConnection();
    this.loadingService.isLoading.next(false);
  }

  getUserInfo(username: string): void {
    this.subscriptions.push(
      this.accountService.getUserInfo(username).subscribe(
        (res: User) => {
          this.user = res;
        }, err => {
          console.log(err);
          this.user = null;
          this.logOut();
          this.router.navigate(['/login']);
        }
      )
    );
  }

  logOut(): void {
    this.accountService.logout();
    this.router.navigate(['/login']);
    this.alertService.showAlert(
      'You need to log in to accecc this page!',
      AlertType.DANGER
    );
  }

  getUserProfile(username: string): void {
    this.router.navigate(['/profile', username]);
  }

  getPosts(): void {
    this.subscriptions.push(
      this.postService.getPosts().subscribe(
        (res: any) => {
          this.posts = res.body;
          this.loadingService.isLoading.next(false);
        }, err => {
          console.log(err);
          this.loadingService.isLoading.next(false);
        }
      )
    );
  }

  onDelete(id: number): void {
    this.subscriptions.push(
      this.postService.delete(id).subscribe(
        res => {
          this.alertService.showAlert('Post was deleted successfully.', AlertType.SUCCESS);
          this.getPosts();
        }, err => {
          this.alertService.showAlert('Post was not deleted!, Please try again.', AlertType.DANGER);
          this.getPosts();
        }
      )
    );
  }

  seeOnePost(postId: number): void {
    this.router.navigate(['/post', postId]);
  }
  initializeWebSocketConnection(): void {
    const stompClient = this.websocket.connect();
    stompClient.connect({}, frame => {
      console.log('----------------> ', frame);
      stompClient.subscribe('/queue', data => {
        if (data) {
          console.log(data);
        } else {
          console.log('not data sent yet');
        }
      })
    });
  }
  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}
