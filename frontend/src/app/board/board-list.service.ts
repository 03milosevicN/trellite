import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BoardListRequestModel, BoardListResponseModel, BoardListUpdateModel } from './board.model';

@Injectable({
  providedIn: 'root'
})
export class BoardListService {

  private API = 'http://localhost:8080/api/board-lists';
  private http = inject(HttpClient);


  getById(id: string) {
    return this.http.get<BoardListResponseModel>(`${this.API}/${id}`);
  }

  getByBoardId(boardId: string) {
    return this.http.get<BoardListResponseModel[]>(`${this.API}/by-board/${boardId}`);
  }

  create(data: BoardListRequestModel) {
    return this.http.post<BoardListResponseModel>(`${this.API}`, data);
  }

  update(id: string, data: BoardListUpdateModel) {
    return this.http.put<BoardListResponseModel>(`${this.API}/${id}`, data);
  }

  delete(id: string) {
    return this.http.delete<void>(`${this.API}/${id}`);
  }

}
