import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { PostService } from './services/post.service';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { LoginComponent } from './components/login/login.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { HomeComponent } from './components/home/home.component';
import { ProfileComponent } from './components/profile/profile.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { ResetPasswordComponent } from './components/reset-password/reset-password.component';
import { PostComponent } from './components/post/post.component';
import { PostDetailsComponent } from './components/post/post-details/post-details.component';
import { AccountService } from './services/account.service';
import { AlertService } from './services/alert.service';
import { LoadingService } from './services/loading.service';
import { AuthenticationGuard } from './guard/authentication.guard';
import { PostResolverService } from './services/post-resolver.service';
import { AuthInterceptor } from './interceptor/auth.interceptor';
import { CacheInterceptorService } from './interceptor/cache-interceptor.interceptor';
import { NgxLoadingModule } from 'ngx-loading';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SocketService } from './services/socket.service';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SignUpComponent,
    HomeComponent,
    ProfileComponent,
    NavbarComponent,
    ResetPasswordComponent,
    PostComponent,
    PostDetailsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgxLoadingModule.forRoot({})
  ],
  providers: [
    AccountService,
    PostService,
    AlertService,
    LoadingService,
    AuthenticationGuard,
    PostResolverService,
    SocketService,
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: CacheInterceptorService, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
