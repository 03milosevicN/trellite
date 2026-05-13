import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {User} from "../models/user.model";
import {Observable} from "rxjs";

@Injectable({
    providedIn: "root",
})
export class UserService {

    MOCK_API_URL: string = 'http://localhost:3000/users';

    private http: HttpClient = inject(HttpClient);


    public getById(id: string): Observable<User> {
        return this.http.get<User>(`${this.MOCK_API_URL}/${id}`);
    }

    public create(data: User): Observable<User> {
        return this.http.post<User>(`${this.MOCK_API_URL}`, data);
    }

}