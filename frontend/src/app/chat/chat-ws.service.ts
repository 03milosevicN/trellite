import { computed, inject, Injectable, signal } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { Client, IMessage, StompSubscription } from '@stomp/stompjs';

import SockJS from 'sockjs-client';
import { ChatConnectionState, ChatMessageModel, ChatMessageRequestModel } from './message.model';


@Injectable({
  providedIn: 'root',
})
export class ChatWebSocketService {
  private authService = inject(AuthService);

  private client: Client | null = null;
  private activeSubscription: StompSubscription | null = null;
  private connectedBoardId: string | null = null;

  private readonly _connectionState = signal<ChatConnectionState>('DISCONNECTED');
  readonly connectionState = this._connectionState.asReadonly();

  private readonly _messages = signal<ChatMessageModel[]>([]);
  readonly messagesState = this._messages.asReadonly();

  readonly isConnected = computed(() => this._connectionState() === 'CONNECTED');

  private pendingSubscribeBoardId: string | null = null;

  connect(boardId: string) {
    if (this.connectedBoardId === boardId && this.client?.activate) {
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
        console.info(`STOMP: ${str}`);
      },
      reconnectDelay: 5000,
      onConnect: () => {
        this._connectionState.set('CONNECTED');
        this.connectedBoardId = boardId;
        if (this.pendingSubscribeBoardId == boardId) {
          this.subscribeToBoard(boardId);
        }
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

  subscribeToBoard(boardId: string) {
    this.pendingSubscribeBoardId = boardId;

    if (!this.client?.connected) {
      return;
    }

    // In case of existing active subscription, unsubscribe
    this.activeSubscription?.unsubscribe();

    this.activeSubscription = this.client.subscribe(
      `/topic/boards/${boardId}/chat`,
      (stompMessage: IMessage) => {
        const incoming = JSON.parse(stompMessage.body);
        this._messages.update((curr) => [...curr, incoming]);
      },
    );
  }

  /**
   * Unsubscribe, but keep connection to board.
   * Runs when chat panel is closed.
   */
  unsubscribeFromBoard() {
    this.activeSubscription?.unsubscribe();
    this.activeSubscription = null;
    this.pendingSubscribeBoardId = null;
  }

  sendMessage(boardId: string, content: string): void {
    if (!this.client?.connected) {
      alert(`Unexpected message: no active connection.`);
    }

    const request: ChatMessageRequestModel = { content };

    this.client?.publish({
      destination: `/app/boards/${boardId}/chat.send`,
      body: JSON.stringify(request),
    });
  }

  disconnect() {
    this.activeSubscription?.unsubscribe();
    this.activeSubscription = null;
    this.pendingSubscribeBoardId = null;
    this.client?.deactivate();
    this.client = null;
    this.connectedBoardId = null;
    this._connectionState.set('DISCONNECTED');
    this._messages.set([]);
  }

}
