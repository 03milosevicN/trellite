import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {OrganizationModel} from "../models/organization.model";
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class OrganizationService {

    API_URL: string = 'http://localhost:8080/api/orgs'

    private http: HttpClient = inject(HttpClient);

    public getById(id: string): Observable<OrganizationModel> {
        return this.http.get<OrganizationModel>(`${this.API_URL}/${id}`);
    }

    public getByOwner(id: string): Observable<OrganizationModel[]> {
        return this.http.get<OrganizationModel[]>(`${this.API_URL}?ownedBy=${id}`);
    }

    public create(data: OrganizationModel): Observable<OrganizationModel> {
        return this.http.post<OrganizationModel>(`${this.API_URL}`, data);
    }

    public delete(id: string): Observable<void> {
        return this.http.delete<void>(`${this.API_URL}/${id}`);
    }

}