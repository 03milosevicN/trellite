import {Component, inject, signal, WritableSignal} from "@angular/core";
import {RegistrationRequestModel} from "../../../models/auth.model";
import {email, FieldTree, form, FormField, min, required} from "@angular/forms/signals";
import {AuthService} from "../../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: "app-register",
  imports: [
    FormField
  ],
  templateUrl: "./register.html",
  styleUrl: "./register.css",
})
export class Register {

  private authService: AuthService = inject(AuthService);
  private router: Router = inject(Router);

  protected registerModel: WritableSignal<RegistrationRequestModel> = signal<RegistrationRequestModel>({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
  });

  protected form: FieldTree<RegistrationRequestModel> = form(this.registerModel, (schemaPath) => {
    required(schemaPath.firstName, {message: 'First name is required.'});
    required(schemaPath.lastName, {message: 'Last name is required.'});
    required(schemaPath.email, {message: 'Email is required.'});
    email(schemaPath.email, {message: 'Email field must match the email format.'});
    required(schemaPath.password, {message: 'Password is required.'});
    min(schemaPath.password, 8, {message: 'Password should be at least 8 characters long.'});
  });

  onSubmit(event: Event): void {
    event.preventDefault();

    const credentials: RegistrationRequestModel = this.registerModel();
    this.authService.register(credentials).subscribe({
      next: (result) => {
        console.log(result);
        this.router.navigate(['/auth/login']);
      },
      error: (err) => {
        console.log(err);
      }
    });
  }

}
