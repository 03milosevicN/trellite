import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ItemModel} from "../models/card.model";
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class ItemService {

    API_URL: string = 'http://localhost:8080/api/items';
    private http: HttpClient = inject(HttpClient);


    public getById(id: string): Observable<ItemModel> {
        return this.http.get<ItemModel>(`${this.API_URL}/${id}`);
    }

    public getAllByChecklistId(id: string): Observable<ItemModel[]> {
        return this.http.get<ItemModel[]>(`${this.API_URL}?checklist_id=${id}`);
    }

    public create(data: ItemModel): Observable<ItemModel> {
        return this.http.post<ItemModel>(`${this.API_URL}`, data);
    }

    public update(id: string, data: ItemModel): Observable<ItemModel> {
        return this.http.put<ItemModel>(`${this.API_URL}/${id}`, data);
    }

    public delete(id: string): Observable<void> {
        return this.http.delete<void>(`${this.API_URL}/${id}`);
    }

}