import { Component, inject, signal } from '@angular/core';
import { AuthService } from './auth.service';
import { Router, RouterLink } from '@angular/router';
import { LoginRequestModel } from './auth.model';
import { email, form, FormField, minLength, required } from '@angular/forms/signals';
import { UserState } from '../states/user.state';

@Component({
  selector: 'app-login',
  imports: [FormField, RouterLink],
  template: `
    <div class="navbar shadow-lg">
      <p class="font-medium text-2xl">Trellite</p>
    </div>
    <div class="hero bg-base-200 min-h-screen">
      <div class="her-content flex-col lg:flex-row-reverse">
        <div class="text-center lg:text-left">
          <h1 class="text-5xl font-bold p-5">Login</h1>
        </div>
        <div class="card bg-base-100 w-full max-w-sm shrink-0">
          <div class="card-body p-10">
            <form (submit)="onSubmit($event)" class="fieldset">
              <label class="label">Email</label>
              <input [formField]="loginForm.email" type="email" class="input border rounded-md" />

              <label class="label">Password</label>
              <input
                [formField]="loginForm.password"
                type="password"
                class="input border rounded-md"
              />

              <button class="btn btn-neutral mt-4">Login</button>
              <button routerLink="/register" class="btn btn-neutral mt-4">
                Don't have an account? Register
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  `,
})
export class Login {

  private authService = inject(AuthService);
  private router = inject(Router);

  protected loginModel = signal<LoginRequestModel>({
    email: '',
    password: '',
  });

  protected loginForm = form(this.loginModel, (schemaPath) => {
    required(schemaPath.email, { message: 'Email is required.' });
    email(schemaPath.email, { message: 'Email field must match the email format.' });
    required(schemaPath.password, { message: 'Password is required.' });
    minLength(schemaPath.password, 8, {
      message: 'Password should be at least 8 characters long.',
    });
  });

  onSubmit(event: Event) {
    event.preventDefault();
    const creds: LoginRequestModel = this.loginModel();
    this.authService.login(creds).subscribe({
      next: (data) => {
        this.router.navigate([`/u/${data.id}`]);
      },
      error: (err) => {
        console.error(`Login error: ${err}`);
      },
    });
  }
}
