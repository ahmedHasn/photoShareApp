import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { Router, ActivatedRoute } from '@angular/router';
import { AccountService } from 'src/app/services/account.service';
import { LoadingService } from 'src/app/services/loading.service';
import { AlertService } from 'src/app/services/alert.service';
import { PostService } from 'src/app/services/post.service';
import { User } from 'src/app/model/user';
import { Post } from 'src/app/model/post';
import { Server } from 'src/app/constants/server';
import { Comment } from 'src/app/model/comment';
import { AlertType } from 'src/app/enum/alert-type.enum';

@Component({
  selector: 'app-post-details',
  templateUrl: './post-details.component.html',
  styleUrls: ['./post-details.component.css']
})
export class PostDetailsComponent implements OnInit , OnDestroy{
  private constant: Server = new Server();
  private subscriptions: Subscription [] = [];
  user: User = new User();
  posts: Post[] = [];
  host: string;
  userHost: string;
  postHost: string;
  username: string;
  comment: Comment = new Comment();
  commentList: Comment[] = [];
  post: Post;
  like: string;
  isUser: boolean;
  postId: number;
  color: string;
  likes: number;

  constructor(
    private router: Router,
    private accountService: AccountService,
    private loadingService: LoadingService,
    private alertService: AlertService,
    private postService: PostService,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.loadingService.isLoading.next(true);
    this.comment.content = '';
    this.resolvePost();
  }

  resolvePost(): void {
    let resolvePost: Post = this.route.snapshot.data['resolvedPost'];
    if (resolvePost) {
      console.log(resolvePost);
      this.post = resolvePost;
      if (this.post.commentList){
        this.commentList = this.post.commentList;
      }
      this.likes = this.post.likes;
      this.userHost = this.constant.userPicture;
      this.postHost = this.constant.postPicture;
      this.host = this.constant.host;
      this.getUserInfo(this.accountService.loggInUsername);
      this.loadingService.isLoading.next(false);
    } else {
      this.loadingService.isLoading.next(false);
      this.alertService.showAlert(
        'Post was not found',
        AlertType.DANGER
      );
      this.router.navigate(['/home']);
    }
  }

  getUserInfo(username: string): void {
    this.subscriptions.push(
      this.accountService.getUserInfo(username).subscribe(
        (res: User) => {
          this.displayLike(res);
          this.user = res;
        }, err => {
          this.user = null;
          this.logOut();
          this.router.navigate(['/login']);
        }
      )
    );
  }

  displayLike(user: User): void {
    const result: Post = user.likedPosts.find(post => post.id === this.post.id );
    if (result) {
      this.like = 'Unlike';
      this.color = '#18BC9C';
    } else {
      this.like = 'Like';
      this.color = '#000000';
    }
  }

  getUserProfile(username: string): void {
    this.router.navigate(['/profile', username]);
  }

  onDelete(id: number): void {
    console.log(id);
    this.subscriptions.push(
      this.postService.delete(id).subscribe(
        res => {
          this.alertService.showAlert('Post was deleted successfully.', AlertType.SUCCESS);
          this.router.navigate(['/home']);
        }, err => {
          this.alertService.showAlert('Post was not deleted!, Please try again.', AlertType.DANGER);
        }
      )
    );
  }

  onAddComment(comment: any, post: Post): void {
    this.comment.content = '';
    const newComment: Comment = new Comment();
    newComment.content = comment.value.content;
    newComment.postId = comment.value.postId;
    newComment.postedDate = new Date();
    newComment.username = comment.value.username;
    this.commentList.push(newComment);
    this.subscriptions.push(
      this.postService.saveComment(newComment).subscribe(
        res => {
          console.log(res);
          console.log('Comment has been saved to the database...');
        }, err => {
          this.loadingService.isLoading.next(false);
          console.log(err);
        }
      )
    );
  }

  likePost(post: Post, user: User) {
    if (this.color === '#000000') {
      this.color = '#18BC9C';
      this.like = 'Unlike';
      this.doLike(post, user);
      this.likes += 1;
    } else {
      this.color = '#000000';
      this.like = 'Like';
      this.doUnlike(post, user);
      if (user.likedPosts) {
        for (let i = 0; i < user.likedPosts.length; i++) {
          if (user.likedPosts[i].id === post.id) {
            user.likedPosts.slice(i, 1);
          }
        }
      }
      if (this.likes > 0) {
        this.likes -= 1;
      }
    }
  }

  doLike(post, user): void {
    this.subscriptions.push(
      this.postService.like(post.id).subscribe(
        res => {
          console.log(res);
        }, err => {
          console.log(err);
        }
      )
    );
  }
  doUnlike(post, user): void {
    this.subscriptions.push(
      this.postService.unLike(post.id).subscribe(
        res => {
          console.log(res);
        }, err => {
          console.log(err);
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

  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}
