import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {UserModel} from "../models/user.model";
import {Observable} from "rxjs";

@Injectable({
    providedIn: "root",
})
export class UserService {

    API_URL: string = 'http://localhost:8080/api/users';
    private http: HttpClient = inject(HttpClient);


    getById(id: string): Observable<UserModel> {
        return this.http.get<UserModel>(`${this.API_URL}/${id}`);
    }

    public create(data: UserModel): Observable<UserModel> {
        return this.http.post<UserModel>(`${this.API_URL}`, data);
    }

}