import { logging } from 'protractor';
import { Subject, Subscription } from 'rxjs';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { PostService } from './services/post.service';
import { Alert } from './model/alert';
import { LoadingService } from './services/loading.service';
import { AlertService } from './services/alert.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy{

  public suscrition: Subscription[] = [];
  public alerts: Alert[] = [];
  public loading: boolean;

  constructor(private postService: PostService, private loadingService: LoadingService, private alertService: AlertService){
    this.loading = false;
  }

  ngOnInit(){
    this.suscrition.push(
      this.loadingService.isLoading.subscribe(isLoading => {
        this.loading = isLoading;
      })
    );
    this.suscrition.push(
      this.alertService.alerts.subscribe(alert => {
        this.alerts.push(alert);
        this.closeAlert(3);
      })
    );
  }

  private closeAlert(second: number): void {
    setTimeout(() => {
      let element: HTMLElement = document.getElementById('dismiss-alert') as HTMLElement;
      element.click();
    }, second * 1000);
  }

  ngOnDestroy(){
    this.suscrition.forEach(sub => sub.unsubscribe());
  }
}
