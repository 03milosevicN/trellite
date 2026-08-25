import { Injectable, signal } from '@angular/core';
import { OrgResponseModel } from '../org/org.model';

@Injectable({
  providedIn: 'root',
})
export class OrgState {

  private activeOrgSignal = signal<OrgResponseModel | null>(this.getStoredOrg());
  readonly activeOrgState = this.activeOrgSignal.asReadonly();

  setActiveOrg(org: OrgResponseModel) {
    localStorage.setItem('ACTIVE_ORG', JSON.stringify(org));
    this.activeOrgSignal.set(org);
  }

  clearState() {
    localStorage.removeItem('ACTIVE_ORG');
    this.activeOrgSignal.set(null);
  }

  private getStoredOrg() {
    const data = localStorage.getItem('ACTIVE_ORG');
    return data ? JSON.parse(data) : null;
  }

}
