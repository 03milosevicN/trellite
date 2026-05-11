export interface Item {
    id: number;
    task: string;
}

export interface Checklist {
    id: number;
    title: string;
    is_completed: boolean;
    items: Item[];
}

export interface Card {
    id: number;
    title: string;
    desc: string;
    assignees: number[];
    due_date: Date;
    checklists?: Checklist[];
}

export interface BoardList {
    id: number;
    title: string;
    created_at: Date
    cards?: Card[]
}

export interface Board {
    id: number;
    org_id: number;
    title: string;
    created_at: Date;
    archived: boolean;
    board_lists?: BoardList[],
}