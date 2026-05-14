import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Organization} from "../models/organization.model";
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class OrganizationService {

    MOCK_API_URL: string = 'http://localhost:3000/api/organizations';

    private http: HttpClient = inject(HttpClient);

    public getById(id: string): Observable<Organization> {
        return this.http.get<Organization>(`${this.MOCK_API_URL}/${id}`);
    }

    public getByOwner(id: string): Observable<Organization[]> {
        return this.http.get<Organization[]>(`${this.MOCK_API_URL}?ownedBy=${id}`);
    }

    public create(data: Organization): Observable<Organization> {
        return this.http.post<Organization>(`${this.MOCK_API_URL}`, data);
    }

    public delete(id: string): Observable<void> {
        return this.http.delete<void>(`${this.MOCK_API_URL}/${id}`);
    }

}