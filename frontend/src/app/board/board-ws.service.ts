import { inject, Injectable, signal } from '@angular/core';
import { Client, IMessage, StompSubscription } from '@stomp/stompjs';
import { BoardEvent } from './board-event.model';
import SockJS from 'sockjs-client';
import { AuthService } from '../auth/auth.service';
import { ChatConnectionState } from '../chat/message.model';

@Injectable({
  providedIn: 'root',
})
export class BoardWebSocketService {
  private authService = inject(AuthService);

  private client: Client | null = null;
  private activeSubscription: StompSubscription | null = null;
  private connectedBoardId: string | null = null;

  private readonly _connectionState = signal<ChatConnectionState>('DISCONNECTED');
  readonly connectionState = this._connectionState.asReadonly();

  private readonly _events = signal<BoardEvent | null>(null);
  readonly latestEvent = this._events.asReadonly();

  connect(boardId: string) {
    if (this.connectedBoardId === boardId && this.client?.active) {
      return;
    }
    if (this.client?.active) {
      this.disconnect();
    }

    this.client = new Client({
      webSocketFactory: () => {
        const token = this.authService.getToken()();
        return new SockJS(`http://localhost:8080/ws?token=${encodeURIComponent(token || '')}`);
      },
      connectHeaders: {
        Authorization: `Bearer ${this.authService.getToken()()}`,
      },
      debug: (str: string) => {
        console.info(`BOARD STOMP: ${str}`);
      },
      reconnectDelay: 5000,
      onConnect: () => {
        this._connectionState.set('CONNECTED');
        this.connectedBoardId = boardId;
        this.subscribeToBoard(boardId);
      },
      onStompError: (frame) => {
        console.error(`STOMP error upon connection ${frame.headers['message']}`);
        this._connectionState.set('ERROR');
      },
      onWebSocketClose: () => {
        this._connectionState.set('DISCONNECTED');
      },
    });

    this.client.activate();
  }

  private subscribeToBoard(boardId: string) {
    if (!this.client?.connected) {
      return;
    }

    this.activeSubscription?.unsubscribe();

    this.activeSubscription = this.client.subscribe(
      `/topic/board/${boardId}`,
      (stompMessage: IMessage) => {
        const event: BoardEvent = JSON.parse(stompMessage.body);
        this._events.set(event);
      },
    );
  }

  disconnect() {
    this.activeSubscription?.unsubscribe();
    this.activeSubscription = null;
    this.client?.deactivate();
    this.client = null;
    this.connectedBoardId = null;
    this._connectionState.set('DISCONNECTED');
  }

}
