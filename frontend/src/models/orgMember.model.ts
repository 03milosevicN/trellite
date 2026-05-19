export interface OrgMemberModel {
    orgMembersId: number;
    userId: number;
    orgId: number;
    role: 'OWNER' | 'MEMBER' | 'ADMIN';
}