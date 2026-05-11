import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {BoardList} from "../models/board.model";

@Injectable({
    providedIn: 'root'
})
export class BoardListService {

    MOCK_API_URL: string = 'http://localhost:3000/BoardLists';

    private http: HttpClient = inject(HttpClient);


    public getById(id: number): Observable<BoardList> {
        return this.http.get<BoardList>(`${this.MOCK_API_URL}/${id}`);
    }

    public getAllByBoardId(id: number): Observable<BoardList[]> {
        return this.http.get<BoardList[]>(`${this.MOCK_API_URL}?board_id=${id}`);
    }

    public create(data: BoardList): Observable<BoardList> {
        return this.http.post<BoardList>(`${this.MOCK_API_URL}`, data);
    }

    public update(id: number, data: BoardList): Observable<BoardList> {
        return this.http.put<BoardList>(`${this.MOCK_API_URL}/${id}`, data);
    }

    public delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.MOCK_API_URL}/${id}`);
    }

}