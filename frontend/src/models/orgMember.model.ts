export interface OrgMember {
    id: number;
    user: number;
    organization: number;
    role: 'OWNER' | 'MEMBER' | 'ADMIN';
}