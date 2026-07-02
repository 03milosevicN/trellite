export interface BoardRequest {
    title: string;
    createdAt: Date;
    archived: boolean;
    members?: number[];
}