export interface OrgRequestModel {
  name: string;
  createdAt: Date;
}

export interface OrgResponseModel {
  orgId: string;
  name: string;
  createdAt: Date;
}

export interface JoinOrgRequestModel {
  orgId: string;
  name: string;
}
