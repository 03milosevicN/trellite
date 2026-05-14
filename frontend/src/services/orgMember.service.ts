import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {OrgMember} from "../models/orgMember.model";
import {map, Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class OrgMemberService {

    MOCK_API_URL: string = 'http://localhost:3000/org-members';

    private http: HttpClient = inject(HttpClient);


    public getById(id: string): Observable<OrgMember> {
        return this.http.get<OrgMember>(`${this.MOCK_API_URL}/${id}`);
    }

    public getByUserId(userId: string): Observable<OrgMember | null> {
        return this.http
            .get<OrgMember[]>(`${this.MOCK_API_URL}?user=${userId}`)
            .pipe(map(results => results[0] ?? null));
    }

    public create(data: OrgMember): Observable<OrgMember> {
        return this.http.post<OrgMember>(`${this.MOCK_API_URL}`, data);
    }

}