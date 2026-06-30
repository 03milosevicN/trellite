import {computed, effect, inject, Injectable, Signal, signal, WritableSignal} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {LoginRequestModel, LoginResponseModel, RegistrationRequestModel} from "../models/auth.model";
import {Observable, tap} from "rxjs";
import {Router} from "@angular/router";
import {UserModel} from "../models/user.model";
import {UserService} from "./user.service";

@Injectable({
    providedIn: 'root',
})
export class AuthService {

    API_URL: string = 'http://localhost:8080/api/auth';

    private http: HttpClient = inject(HttpClient);
    private router: Router = inject(Router);
    private userService: UserService = inject(UserService);

    private tokenSignal: WritableSignal<string | null> = signal<string | null>(localStorage.getItem('access_token'));
    private cachedUserProfileSignal: WritableSignal<UserModel | null> = signal<UserModel | null>(null);
    public userIdPointer = computed( () => this.cachedUserProfileSignal()?.userId ?? null );

    /**
     * Upon service call, construction calls fetchProfileByEmail() and stores data.
     */
    constructor() {
        effect(() => {
            const idExtraction: number | null = this.currentUserId();
            if (idExtraction) {
                this.fetchProfileById(this.currentUserId()?.toString()!).subscribe({
                    error: err => console.error(`Failed to fetch id :  ${err}`)
                });
            }
        });
    }


    register(req: RegistrationRequestModel): Observable<RegistrationRequestModel> {
        return this.http.post<RegistrationRequestModel>(`${this.API_URL}/register`, req);
    }

    login(req: LoginRequestModel): Observable<LoginResponseModel> {
        return this.http.post<LoginResponseModel>(`${this.API_URL}/login`, req).pipe(
            tap(response => {
                if (response.token) {
                    localStorage.setItem('access_token', response.token);
                    this.tokenSignal.set(response.token);
                }
            })
        );
    }

    logout(): void {
        localStorage.removeItem('access_token');
        this.tokenSignal.set(null);
        this.cachedUserProfileSignal.set(null);
        this.router.navigate(['/']);
    }

    isLoggedIn(): boolean {
        return !!localStorage.getItem('access_token');
    }

    /**
     * Decode JWT, extract id from payload.
     */
    currentUserId: Signal<any> = computed(() => {
        const token: string | null = this.tokenSignal();
        if (!token) return null;
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            console.log(`IS PAYLOAD THE ID? ${payload.userId}`);
            return payload.userId;
        } catch (e) {
            console.error(`Failed to parse JWT: ${e}`);
            return null;
        }
    });

    fetchProfileById(id: string): Observable<UserModel> {
        return this.userService.getById(id).pipe(
            tap(profile => this.cachedUserProfileSignal.set(profile))
        );
    }

}