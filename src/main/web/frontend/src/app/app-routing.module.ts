import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { ResetPasswordComponent } from './components/reset-password/reset-password.component';
import { HomeComponent } from './components/home/home.component';
import { AuthenticationGuard } from './guard/authentication.guard';
import { PostDetailsComponent } from './components/post/post-details/post-details.component';
import { PostResolverService } from './services/post-resolver.service';
import { ProfileComponent } from './components/profile/profile.component';

const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'sign-up', component: SignUpComponent},
  {path: 'reset-password', component: ResetPasswordComponent},
  {path: '', redirectTo: '/home', pathMatch: 'full'},
  {path: 'home', component: HomeComponent, canActivate: [AuthenticationGuard]},
  {path: 'post/:postId', component: PostDetailsComponent, resolve: {resolvedPost: PostResolverService}, canActivate: [AuthenticationGuard]},
  {path: 'profile/:username', component: ProfileComponent, canActivate: [AuthenticationGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
