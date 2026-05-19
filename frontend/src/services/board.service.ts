import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {BoardModel} from "../models/board.model";

@Injectable({
    providedIn: 'root'
})
export class BoardService {

    API_URL: string = 'http://localhost:8080/api/boards';
    private http: HttpClient = inject(HttpClient);


    public getById(id: string): Observable<BoardModel> {
        return this.http.get<BoardModel>(`${this.API_URL}/${id}`);
    }

    public getAllByOrgId(id: string): Observable<BoardModel[]> {
        return this.http.get<BoardModel[]>(`${this.API_URL}?org_id=${id}`);
    }

    public create(data: BoardModel): Observable<BoardModel> {
        return this.http.post<BoardModel>(`${this.API_URL}`, data);
    }

    public update(id: string, data: BoardModel): Observable<BoardModel> {
        return this.http.put<BoardModel>(`${this.API_URL}/${id}`, data);
    }

    public delete(id: string): Observable<void> {
        return this.http.delete<void>(`${this.API_URL}/${id}`);
    }

}