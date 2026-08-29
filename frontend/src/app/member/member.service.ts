import { inject, Injectable } from '@angular/core';
import { OrgResponseModel } from '../org/org.model';
import { HttpClient } from '@angular/common/http';
import { UserResponseModel } from '../user/user.model';
import { MemberResponseModel, RoleType } from './member.model';

@Injectable({
  providedIn: 'root',
})
export class MemberService {

  private API: string = 'http://localhost:8080/api/members';
  private http = inject(HttpClient);


  public getOrgMemberships(orgId: string) {
    return this.http.get<OrgResponseModel[]>(`${this.API}/user/${orgId}/memberships`);
  }

  public getUserMemberships(userId: string) {
    return this.http.get<OrgResponseModel[]>(`${this.API}/user/${userId}/memberships`);
  }

  public getUserAdminships(userId: string) {
    return this.http.get<OrgResponseModel[]>(`${this.API}/user/${userId}/adminships`);
  }

  public getBoardMemberships(orgId: string) {
    return this.http.get<string[]>(`${this.API}/organizations/${orgId}/boards/memberships`);
  }

  public getOrgMembers(orgId: string) {
    return this.http.get<MemberResponseModel[]>(`${this.API}/by-org/${orgId}`);
  }

  public hasRole(orgId: string, role: RoleType) {
    return this.http.get<boolean>(`${this.API}/check`);
  }

  public leaveOrg(orgId: string) {
    return this.http.delete<void>(`${this.API}/${orgId}`);
  }

}
