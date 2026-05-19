import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {CardModel} from "../models/card.model";

@Injectable({
    providedIn: 'root'
})
export class CardService {

    API_URL: string = 'http://localhost:8080/cards';
    private http: HttpClient = inject(HttpClient);


    public getById(id: string): Observable<CardModel> {
        return this.http.get<CardModel>(`${this.API_URL}/${id}`);
    }

    public getAllByBoardListId(id: string): Observable<CardModel[]> {
        return this.http.get<CardModel[]>(`${this.API_URL}?board_list_id=${id}`);
    }

    public create(data: CardModel): Observable<CardModel> {
        return this.http.post<CardModel>(`${this.API_URL}`, data);
    }

    public update(id: string, data: CardModel): Observable<CardModel> {
        return this.http.put<CardModel>(`${this.API_URL}/${id}`, data);
    }

    public delete(id: string): Observable<void> {
        return this.http.delete<void>(`${this.API_URL}/${id}`);
    }

}