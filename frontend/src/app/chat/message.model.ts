export interface ChatMessageModel {
  id: string;
  boardId: string;
  senderId: number;
  senderName: string;
  content: string;
  sentAt: string;
}

export interface ChatMessageRequestModel {
  content: string;
}

export interface ChatHistoryPageModel {
  content: ChatMessageModel[];
  totalElements: number;
  totalPages: number;
  number: number;
}

export type ChatConnectionState = 'DISCONNECTED' | 'CONNECTING' | 'CONNECTED' | 'ERROR';
