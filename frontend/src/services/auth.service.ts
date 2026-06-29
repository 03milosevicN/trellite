import {inject, Injectable, signal, WritableSignal} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {LoginRequestModel, LoginResponseModel, RegistrationRequestModel} from "../models/auth.model";
import {Observable, shareReplay, tap} from "rxjs";
import {Router} from "@angular/router";

@Injectable({
    providedIn: 'root',
})
export class AuthService {

    API_URL: string = 'http://localhost:8080/api/auth';
    private http: HttpClient = inject(HttpClient);

    private router: Router = inject(Router);

    register(req: RegistrationRequestModel): Observable<RegistrationRequestModel> {
        return this.http.post<RegistrationRequestModel>(`${this.API_URL}/register`, req);
    }

    login(req: LoginRequestModel): Observable<LoginResponseModel> {
        return this.http.post<LoginResponseModel>(`${this.API_URL}/login`, req).pipe(
            tap(response => {
                if (response.token) {
                    localStorage.setItem('access_token', response.token);
                }
            })
        );
    }

    logout(): void {
        localStorage.removeItem('access_token');
        this.router.navigate(['/']);
    }

    isLoggedIn(): boolean {
        return !!localStorage.getItem('access_token');
    }

}