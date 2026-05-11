import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Board} from "../models/board.model";

@Injectable({
    providedIn: 'root'
})
export class BoardService {

    MOCK_API_URL: string = 'http://localhost:3000/boards';

    private http: HttpClient = inject(HttpClient);


    public getById(id: number): Observable<Board> {
        return this.http.get<Board>(`${this.MOCK_API_URL}/${id}`);
    }

    public getAllByOrgId(id: number): Observable<Board[]> {
        return this.http.get<Board[]>(`${this.MOCK_API_URL}?org_id=${id}`);
    }

    public create(data: Board): Observable<Board> {
        return this.http.post<Board>(`${this.MOCK_API_URL}`, data);
    }

    public update(id: number, data: Board): Observable<Board> {
        return this.http.put<Board>(`${this.MOCK_API_URL}/${id}`, data);
    }

    public delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.MOCK_API_URL}/${id}`);
    }

}