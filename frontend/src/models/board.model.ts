export interface BoardModel {
    id: string;
    orgId: number;
    title: string;
    members: number[];
    createdAt: Date;
    archived: boolean;
}