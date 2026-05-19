export interface ItemModel {
    id: string;
    task: string;
}

export interface ChecklistModel {
    id: string;
    title: string;
    isCompleted: boolean;
    items: ItemModel[];
}

export interface CardModel {
    id: string;
    boardListId: string;
    title: string;
    desc: string;
    assignees: number[];
    labels: string[];
    dueDate: Date;
    checklists: ChecklistModel[];
}