import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserResponseModel } from './user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private API = 'http://localhost:8080/api/users';
  private http = inject(HttpClient);


  getAll() {
    return this.http.get<UserResponseModel[]>(`${this.API}`);
  }

  getById(userId: string) {
    return this.http.get<UserResponseModel>(`${this.API}/${userId}`);
  }

}
