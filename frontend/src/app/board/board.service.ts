import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BoardRequestModel, BoardResponseModel } from './board.model';
import { MemberResponseModel } from '../member/member.model';

@Injectable({
  providedIn: 'root',
})
export class BoardService {

    private API = 'http://localhost:8080/api/boards';
    private http = inject(HttpClient);


    getById(boardId: string) {
      return this.http.get<BoardResponseModel>(`${this.API}/${boardId}`);
    }

    getByBoardId(boardId: string) {
      return this.http.get<BoardResponseModel>(`${this.API}/by-board/${boardId}`);
    }

    getByOwningOrgId(orgId: string) {
      return this.http.get<BoardResponseModel[]>(`${this.API}/org/${orgId}`);
    }

    getMembers(orgId: string) {
      return this.http.get<MemberResponseModel[]>(`${this.API}/${orgId}/members`);
    }

    getBoardMembers(boardId: string) {
      return this.http.get<MemberResponseModel[]>(`${this.API}/${boardId}/board-members`);
    }

    create(data: BoardRequestModel) {
      return this.http.post<BoardResponseModel>(`${this.API}`, data);
    }

    update(boardId: string, data: BoardRequestModel) {
      return this.http.put<BoardResponseModel>(`${this.API}/${boardId}`, data);
    }

    delete(boardId: string) {
      return this.http.delete<void>(`${this.API}/${boardId}/del`);
    }

    assignToBoard(userId: string, boardId: string) {
      return this.http.post<BoardResponseModel>(`${this.API}/${userId}/${boardId}`, null);
    }

    leaveBoard(boardId: string, userId: string) {
      return this.http.delete<void>(`${this.API}/${boardId}?userId=${userId}`);
    }

}
