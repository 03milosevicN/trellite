import {Component, inject, OnInit, signal, WritableSignal} from "@angular/core";
import {UserService} from "../../services/user.service";
import {ActivatedRoute, RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";
import {UserModel} from "../../models/user.model";
import {LucideCalendarClock, LucideCog, LucideCreditCard, LucideUsers, LucideWalletCards} from "@lucide/angular";
import {OrganizationModel} from "../../models/organization.model";
import {OrgMemberService} from "../../services/orgMember.service";
import {forkJoin} from "rxjs";

@Component({
  selector: "app-user",
  imports: [
    LucideCog,
    LucideCreditCard,
    LucideWalletCards,
    LucideCalendarClock,
    LucideUsers,
    RouterLink,
    RouterOutlet,
    RouterLinkActive
  ],
  templateUrl: "./user.html",
  styleUrl: "./user.css",
})
export class User implements OnInit {

  protected userSignal: WritableSignal<UserModel | null> = signal<UserModel | null>(null);

  protected orgsSignal: WritableSignal<OrganizationModel[] | null> = signal<OrganizationModel[] | null>(null);

  protected userId: string = '';

  private userService: UserService = inject(UserService);
  private orgMemberService: OrgMemberService = inject(OrgMemberService);

  private activatedRoute: ActivatedRoute = inject(ActivatedRoute);


  ngOnInit(): void {
    this.userId = this.activatedRoute.snapshot.paramMap.get('userId')!;
    this.loadData();
  }


  loadData(): void {
    forkJoin({
      user: this.userService.getById(this.userId!),
      orgs: this.orgMemberService.getAllOrgsByUserId(this.userId!),
    }).subscribe({
      next: ({user, orgs}) => {

        if (user == null || orgs == null) {
          console.log('One of the fetched data pairs is currently null');
          this.userSignal.set(null);
          this.orgsSignal.set(null);
          return;
        }
        this.userSignal.set(user);
        this.orgsSignal.update( () => [...orgs]);
      },
      error: error => {
        console.log('Issue with loading data: ', error);
      }
    });
  }

}
