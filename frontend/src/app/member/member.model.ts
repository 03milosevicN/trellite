import { UserResponseModel } from '../user/user.model';
import { OrgResponseModel } from '../org/org.model';

export interface MemberRequestModel {
  userId: number;
  orgId: number;
  role: RoleType;
}

export interface MemberResponseModel {
  memberId: number;
  userResponse: UserResponseModel;
  orgResponse: OrgResponseModel;
  role: RoleType;
}

export type RoleType = 'MEMBER' | 'ADMIN';
