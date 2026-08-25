import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ChatHistoryPageModel } from './message.model';

@Injectable({
  providedIn: 'root',
})
export class ChatService {

  private API: string = 'http://localhost:8080/api/boards';
  private http = inject(HttpClient);


  /**
   * Hydration of chat panel with previous messages via pagination.
   * @param boardId - Board ID
   * @param page - Explicitly begin pagination at first page.
   * @param size - Page size equals 30 entries
   */
  getHistory(boardId: string, page: number = 0, size: number = 10,): Observable<ChatHistoryPageModel> {
    return this.http.get<ChatHistoryPageModel>(
      `${this.API}/${boardId}/chat?page=${page}&size=${size}&sort=sentAt,desc`,
    );
  }

}
