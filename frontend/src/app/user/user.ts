import { Component, inject, input } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { UserService } from './user.service';
import { UserState } from '../states/user.state';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user',
  imports: [],
  template: `

  `
})
export class User {

  private authService = inject(AuthService);
  private userService = inject(UserService);
  private userState = inject(UserState);
  private router = inject(Router);

  userId = input.required<string>();


  logout() {
    this.authService.logout();
  }

}
