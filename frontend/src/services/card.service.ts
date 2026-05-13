import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Card} from "../models/board.model";

@Injectable({
    providedIn: 'root'
})
export class CardService {

    MOCK_API_URL: string = 'http://localhost:3000/cards';

    private http: HttpClient = inject(HttpClient);


    public getById(id: string): Observable<Card> {
        return this.http.get<Card>(`${this.MOCK_API_URL}/${id}`);
    }

    public getAllByBoardListId(id: string): Observable<Card[]> {
        return this.http.get<Card[]>(`${this.MOCK_API_URL}?board_list_id=${id}`);
    }

    public create(data: Card): Observable<Card> {
        return this.http.post<Card>(`${this.MOCK_API_URL}`, data);
    }

    public update(id: string, data: Card): Observable<Card> {
        return this.http.put<Card>(`${this.MOCK_API_URL}/${id}`, data);
    }

    public delete(id: string): Observable<void> {
        return this.http.delete<void>(`${this.MOCK_API_URL}/${id}`);
    }

}