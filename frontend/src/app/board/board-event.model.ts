export interface BoardEvent {
  boardId: string;
  action: EventType;
}

export type EventType = 'LIST_CREATED' | 'LIST_UPDATED' | 'LIST_DELETED'| 'CARD_CREATED' | 'CARD_UPDATED' | 'CARD_DELETED';
