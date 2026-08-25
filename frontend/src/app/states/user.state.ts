import { Injectable, signal } from '@angular/core';
import { UserResponseModel } from '../user/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserState {

  private activeUserSignal = signal<UserResponseModel | null>(this.getStoredUser());
  readonly activeUserState = this.activeUserSignal.asReadonly();

  setActiveUser(user: UserResponseModel) {
    localStorage.setItem('ACTIVE_USER', JSON.stringify(user));
    this.activeUserSignal.set(user);
  }

  clearState() {
    localStorage.removeItem('ACTIVE_USER');
    this.activeUserSignal.set(null);
  }

  private getStoredUser() {
    const data = localStorage.getItem('ACTIVE_USER');
    return data ? JSON.parse(data) : null;
  }

}
