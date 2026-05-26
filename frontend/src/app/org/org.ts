import {Component, inject, signal, WritableSignal} from "@angular/core";
import {OrganizationModel} from "../../models/organization.model";
import {OrgMemberService} from "../../services/orgMember.service";
import {Router} from "@angular/router";

@Component({
  selector: "app-org",
  imports: [],
  templateUrl: "./org.html",
  styleUrl: "./org.css",
})
export class Org {

  protected orgsSignal: WritableSignal<OrganizationModel[] | null> = signal<OrganizationModel[] | null>(null);

  private orgMemberService:OrgMemberService = inject(OrgMemberService);

  private router: Router = inject(Router);
  protected routeUserId: string = '';

  constructor() {
    this.routeUserId = this.router.url[6];
    this.loadData();
  }


  loadData(): void {
    this.orgMemberService.getAllOrgsByUserId(this.routeUserId!).subscribe({
      next: data => {
        this.orgsSignal.set(data);
      }, error: err => {
        console.error('Issue with loading data, ', err);
      }
    })
  }

}
