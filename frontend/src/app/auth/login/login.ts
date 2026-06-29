import {Component, inject, signal, WritableSignal} from "@angular/core";
import {AuthService} from "../../../services/auth.service";
import {Router} from "@angular/router";
import {LoginRequestModel, LoginResponseModel} from "../../../models/auth.model";
import {email, FieldTree, min, required, form, FormField} from "@angular/forms/signals";

@Component({
    selector: "app-login",
    imports: [
        FormField
    ],
    templateUrl: "./login.html",
    styleUrl: "./login.css",
})
export class Login {

    private authService: AuthService = inject(AuthService);
    private router: Router = inject(Router);

    protected loginModel: WritableSignal<LoginRequestModel> = signal<LoginRequestModel>({
        email: '',
        password: '',
    });

    protected form: FieldTree<LoginRequestModel> = form(this.loginModel, (schemaPath) => {
        required(schemaPath.email, {message: 'Email is required.'});
        email(schemaPath.email, {message: 'Email field must match the email format.'});
        required(schemaPath.password, {message: 'Password is required.'});
        min(schemaPath.password, 8, {message: 'Password should be at least 8 characters long.'});
    });

    onSubmit(event: Event): void {
        event.preventDefault();

        const credentials: LoginRequestModel = this.loginModel();

        this.authService.login(credentials).subscribe({
          next: (data) => {
                console.log(`logged user's id: ${data.id}`);
                console.log(data.token);
                this.router.navigate([`/u/${data.id}`]);
            },
            error: (err) => {
                console.log(`Error during login:  ${err}`);
            }
        });
    }

}
