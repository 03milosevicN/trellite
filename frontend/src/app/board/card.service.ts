import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {
  BoardListResponseModel,
  CardRequestModel,
  CardResponseModel,
  CardUpdateModel,
} from './board.model';

@Injectable({
  providedIn: 'root',
})
export class CardService {
  private API = 'http://localhost:8080/api/cards';
  private http = inject(HttpClient);

  getById(id: string) {
    return this.http.get<CardResponseModel>(`${this.API}/${id}`);
  }

  getByBoardId(boardId: string) {
    return this.http.get<CardResponseModel[]>(`${this.API}/by-board/${boardId}`);
  }

  getByBoardListId(boardListId: string) {
    return this.http.get<CardResponseModel[]>(`${this.API}/by-list/${boardListId}`);
  }

  getMyBacklog() {
    return this.http.get<CardResponseModel[]>(`${this.API}/my-backlog`);
  }

  create(data: CardRequestModel) {
    return this.http.post<CardResponseModel>(`${this.API}`, data);
  }

  update(id: string, data: CardUpdateModel) {
    return this.http.put<CardResponseModel>(`${this.API}/${id}`, data);
  }

  delete(id: string) {
    return this.http.delete<void>(`${this.API}/${id}`);
  }

}
