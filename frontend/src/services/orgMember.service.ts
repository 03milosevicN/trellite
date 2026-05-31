import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {OrgMemberModel} from "../models/orgMember.model";
import {map, Observable} from "rxjs";
import {CardModel} from "../models/card.model";
import {BoardModel} from "../models/board.model";
import {BoardListModel} from "../models/boardList.model";
import {OrganizationModel} from "../models/organization.model";
import {UserModel} from "../models/user.model";

@Injectable({
    providedIn: 'root'
})
export class OrgMemberService {

    API_URL: string = 'http://localhost:8080/api/org-members';
    private http: HttpClient = inject(HttpClient);


    public getUserByOrgId(orgId: string): Observable<OrgMemberModel | null> {
        return this.http
            .get<OrgMemberModel[]>(`${this.API_URL}?orgId=${orgId}`)
            .pipe(map(results => results[0] ?? null));
    }

    public getByUserId(userId: string): Observable<OrgMemberModel | null> {
        return this.http
            .get<OrgMemberModel[]>(`${this.API_URL}?userId=${userId}`)
            .pipe(map(results => results[0] ?? null));
    }

    public getAllCardsByUserIdViaBoards(userId: string): Observable<CardModel[] | null> {
        return this.http.get<CardModel[]>(`${this.API_URL}/${userId}/cards`);
    }

    public getAllBoardsByUserId(userId: string): Observable<BoardModel[] | null> {
        return this.http.get<BoardModel[]>(`${this.API_URL}/${userId}/boards`);
    }


    public getAllBoardListsByUserId(userId: string): Observable<BoardListModel[] | null> {
        return this.http.get<BoardListModel[]>(`${this.API_URL}/${userId}/board-lists`);
    }

    public create(data: OrgMemberModel): Observable<OrgMemberModel> {
        return this.http.post<OrgMemberModel>(`${this.API_URL}`, data);
    }

    public getAllOrgsByUserId(userId: string): Observable<OrganizationModel[]> {
        return this.http.get<OrganizationModel[]>(`${this.API_URL}/${userId}/orgs`);
    }

}