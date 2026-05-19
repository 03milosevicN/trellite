import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {BoardListModel} from "../models/boardList.model";

@Injectable({
    providedIn: 'root'
})
export class BoardListService {

    MOCK_API_URL: string = 'http://localhost:3000/BoardLists';

    private http: HttpClient = inject(HttpClient);


    public getById(id: string): Observable<BoardListModel> {
        return this.http.get<BoardListModel>(`${this.MOCK_API_URL}/${id}`);
    }

    public getAllByBoardId(id: string): Observable<BoardListModel[]> {
        return this.http.get<BoardListModel[]>(`${this.MOCK_API_URL}?board_id=${id}`);
    }

    public create(data: BoardListModel): Observable<BoardListModel> {
        return this.http.post<BoardListModel>(`${this.MOCK_API_URL}`, data);
    }

    public update(id: string, data: BoardListModel): Observable<BoardListModel> {
        return this.http.put<BoardListModel>(`${this.MOCK_API_URL}/${id}`, data);
    }

    public delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.MOCK_API_URL}/${id}`);
    }

}