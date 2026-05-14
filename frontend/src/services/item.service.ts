import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Item} from "../models/board.model";
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class ItemService {

    MOCK_API_URL: string = 'http://localhost:3000/items';

    private http: HttpClient = inject(HttpClient);


    public getById(id: string): Observable<Item> {
        return this.http.get<Item>(`${this.MOCK_API_URL}/${id}`);
    }

    public getAllByChecklistId(id: string): Observable<Item[]> {
        return this.http.get<Item[]>(`${this.MOCK_API_URL}?checklist_id=${id}`);
    }

    public create(data: Item): Observable<Item> {
        return this.http.post<Item>(`${this.MOCK_API_URL}`, data);
    }

    public update(id: string, data: Item): Observable<Item> {
        return this.http.put<Item>(`${this.MOCK_API_URL}/${id}`, data);
    }

    public delete(id: string): Observable<void> {
        return this.http.delete<void>(`${this.MOCK_API_URL}/${id}`);
    }

}