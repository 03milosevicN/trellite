import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ChecklistModel} from "../models/card.model";
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class ChecklistService {

    MOCK_API_URL: string = 'http://localhost:3000/checklists';
    private http: HttpClient = inject(HttpClient);


    public getById(id: string): Observable<ChecklistModel> {
        return this.http.get<ChecklistModel>(`${this.MOCK_API_URL}/${id}`);
    }

    public getAllByCardId(id: string): Observable<ChecklistModel[]> {
        return this.http.get<ChecklistModel[]>(`${this.MOCK_API_URL}?card_id=${id}`);
    }

    public create(data: ChecklistModel): Observable<ChecklistModel> {
        return this.http.post<ChecklistModel>(`${this.MOCK_API_URL}`, data);
    }

    public update(id: string, data: ChecklistModel): Observable<ChecklistModel> {
        return this.http.put<ChecklistModel>(`${this.MOCK_API_URL}/${id}`, data);
    }

    public delete(id: string): Observable<void> {
        return this.http.delete<void>(`${this.MOCK_API_URL}/${id}`);
    }

}