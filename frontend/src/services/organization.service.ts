import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {OrganizationModel} from "../models/organization.model";
import {Observable} from "rxjs";
import {OrganizationRequestModel} from "../models/organization-request.model";

@Injectable({
    providedIn: 'root'
})
export class OrganizationService {

    API_URL: string = 'http://localhost:8080/api/orgs'

    private http: HttpClient = inject(HttpClient);


    public getByOwner(ownerId: string): Observable<OrganizationModel[]> {
        return this.http.get<OrganizationModel[]>(`${this.API_URL}/owner/${ownerId}`);
    }


    public create(data: OrganizationRequestModel): Observable<OrganizationModel> {
        return this.http.post<OrganizationModel>(`${this.API_URL}`, data);
    }

    public delete(id: string): Observable<void> {
        return this.http.delete<void>(`${this.API_URL}/${id}`);
    }

}