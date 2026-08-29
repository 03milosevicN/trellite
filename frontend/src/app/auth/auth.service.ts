import { computed, effect, inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { UserService } from '../user/user.service';
import { UserState } from '../states/user.state';
import { LoginRequestModel, LoginResponseModel, RegistrationRequestModel } from './auth.model';
import { tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private API = 'http://localhost:8080/api/auth';
  private http = inject(HttpClient);
  private router = inject(Router);
  private userService = inject(UserService);
  private tokenSignal = signal<string | null>(localStorage.getItem('TOKEN'));
  private userState = inject(UserState);

  currentUserId = computed(() => {
    const token: string | null = this.tokenSignal();
    if (!token) return null;
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.userId ?? payload.id ?? null;
    } catch (e) {
      console.error(`Failed to parse JWT: ${e}`);
      return null;
    }
  });

  constructor() {
    effect(() => {
      const extractedId: number | null = this.currentUserId();
      if (extractedId) {
        this.fetchUserById(this.currentUserId().toString()).subscribe({
          error: err => console.error(`Failed to fetch current user id: ${err}`)
        });
      }
    });
  }

  register(req: RegistrationRequestModel) {
    return this.http.post<RegistrationRequestModel>(`${this.API}/register`, req);
  }

  login(req: LoginRequestModel) {
    return this.http.post<LoginResponseModel>(`${this.API}/login`, req).pipe(
      tap(res => {
        if (res.token) {
          localStorage.setItem('TOKEN', res.token);
          this.tokenSignal.set(res.token);
        }
      })
    );
  }

  logout() {
    localStorage.removeItem('TOKEN');
    this.tokenSignal.set(null);
    this.userState.clearState();
    this.router.navigate(['/login']);
  }

  isLoggedIn() {
    return !!localStorage.getItem('TOKEN');
  }

  fetchUserById(userId: string) {
    return this.userService.getById(userId).pipe(
      tap(state => this.userState.setActiveUser(state)),
    );
  }

  getToken() {
    return this.tokenSignal.asReadonly();
  }

}
