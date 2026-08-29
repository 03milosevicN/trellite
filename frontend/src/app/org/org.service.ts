import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { JoinOrgRequestModel, OrgRequestModel, OrgResponseModel } from './org.model';

@Injectable({
  providedIn: 'root'
})
export class OrgService {

  private API = 'http://localhost:8080/api/orgs';
  private http = inject(HttpClient);


  getAll() {
    return this.http.get<OrgResponseModel[]>(`${this.API}`);
  }

  getById(orgId: string) {
    return this.http.get<OrgResponseModel>(`${this.API}/${orgId}`);
  }

  getByOwner(userId: string) {
    return this.http.get<OrgResponseModel[]>(`${this.API}/owner/${userId}`);
  }

  create(data: OrgRequestModel) {
    return this.http.post<OrgResponseModel>(`${this.API}`, data);
  }

  join(data: JoinOrgRequestModel) {
    const params = new HttpParams().set('userId', data.userId);
    return this.http.post<null>(`${this.API}/${data.orgId}`, null, {params});
  }

  delete(orgId: string) {
    return this.http.delete<OrgResponseModel>(`${this.API}/${orgId}`);
  }

}
