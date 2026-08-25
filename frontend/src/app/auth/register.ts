import { Component, inject, signal } from '@angular/core';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';
import { RegistrationRequestModel } from './auth.model';
import { email, form, FormField, minLength, required } from '@angular/forms/signals';

@Component({
  selector: 'app-register',
  imports: [FormField],
  template: `
    <div class="hero bg-base-200 min-h-screen">
      <div class="her-content flex-col lg:flex-row-reverse">
        <div class="text-center lg:text-left">
          <h1 class="text-5xl font-bold p-5">Register</h1>
        </div>
        <div class="card bg-base-100 w-full max-w-sm shrink-0">
          <div class="card-body p-10">
            <form (submit)="onSubmit($event)" class="fieldset">
              <label class="label">First name</label>
              <input [formField]="registerForm.firstName" class="input" placeholder="First name" />

              <label class="label">Last name</label>
              <input [formField]="registerForm.lastName" class="input" placeholder="Last" />

              <label class="label">Email</label>
              <input
                [formField]="registerForm.email"
                type="email"
                class="input"
                placeholder="Email"
              />

              <label class="label">Password</label>
              <input
                [formField]="registerForm.password"
                type="password"
                class="input"
                placeholder="Password"
              />

              <button class="btn btn-neutral mt-4">Register</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  `,
})
export class Register {

  private authService = inject(AuthService);
  private router = inject(Router);

  protected registerModel = signal<RegistrationRequestModel>({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
  });

  protected registerForm = form(this.registerModel, (schemaPath) => {
    required(schemaPath.firstName, { message: 'First name is required.' });
    required(schemaPath.lastName, { message: 'Last name is required.' });
    required(schemaPath.email, { message: 'Email is required.' });
    email(schemaPath.email, { message: 'Email field must match the format.' });
    required(schemaPath.password, { message: 'Password is required.' });
    minLength(schemaPath.password, 8, { message: 'Password must be at least 8 characters.' });
  });

  onSubmit(event: Event) {
    event.preventDefault();
    const creds: RegistrationRequestModel = this.registerModel();
    this.authService.register(creds).subscribe({
      next: () => { this.router.navigate(['/login']); },
      error: (error) => { console.error(`Registration error: ${error}`); },
    });
  }

}
