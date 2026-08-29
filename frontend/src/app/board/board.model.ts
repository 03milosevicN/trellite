export interface BoardRequestModel {
  orgId?: number | null;
  title: string;
  createdAt: Date;
  archived: boolean;
  members?: number[];
}
export interface BoardResponseModel {
  id: string;
  orgId: number;
  title: string;
  createdAt: Date;
  archived: boolean;
  members?: number[];
}

export interface BoardListRequestModel {
  boardId: string;
  title: string;
  createdAt: Date;
}
export interface BoardListResponseModel {
  id: string;
  boardId: string;
  title: string;
  createdAt: Date;
}
export interface BoardListUpdateModel {
  id?: string;
  boardId: string;
  title?: string;
  updatedAt: Date;
}

export interface CardRequestModel {
  boardListId?: string;
  boardId: string;
  title: string;
  desc: string;
  assignees: number[];
  labels?: string[];
  dueDate?: Date;
  checklists?: ChecklistRequestModel[];
}
export interface CardResponseModel {
  id: string;
  boardListId?: string;
  boardId: string;
  title: string;
  desc: string;
  assignees: number[];
  labels?: string[];
  dueDate?: Date;
  checklists?: ChecklistResponseModel[];
}
export interface CardUpdateModel {
  id: string;
  boardListId?: string;
  boardId: string;
  title?: string;
  desc?: string;
  assignees?: number[];
  labels?: string[];
  dueDate?: Date;
  checklists?: ChecklistResponseModel[];
}

export interface ChecklistRequestModel {
  title: string;
  isCompleted: boolean;
  items?: ItemRequestModel[];
}
export interface ChecklistResponseModel {
  id: string;
  cardId: string;
  title: string;
  isCompleted: boolean;
  items?: ItemResponseModel[];
}
export interface ChecklistUpdateModel {
  id: string;
  cardId: string;
  title?: string;
  isCompleted?: boolean;
  items?: ItemResponseModel[];
}

export interface ItemRequestModel {
  task: string;
}
export interface ItemResponseModel {
  id: string;
  task: string;
}
export interface ItemUpdateModel {
  id: string;
  task?: string;
}
