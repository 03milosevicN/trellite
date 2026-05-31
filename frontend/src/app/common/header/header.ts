import {Component, inject, OnInit, signal, WritableSignal} from "@angular/core";
import {UserModel} from "../../../models/user.model";
import {OrganizationModel} from "../../../models/organization.model";
import {UserService} from "../../../services/user.service";
import {ActivatedRoute, ActivatedRouteSnapshot, Router, RouterLink} from "@angular/router";
import {NgOptimizedImage, UpperCasePipe} from "@angular/common";
import {OrganizationService} from "../../../services/organization.service";
import {OrgMemberService} from "../../../services/orgMember.service";
import {forkJoin, map, of, switchMap} from "rxjs";

@Component({
  selector: "app-header",
  imports: [
    NgOptimizedImage,
    UpperCasePipe,
    RouterLink
  ],
  templateUrl: "./header.html",
  styleUrl: "./header.css",
})
export class Header implements OnInit {

  protected user: WritableSignal<UserModel | null> = signal<UserModel | null>(null);
  protected org: WritableSignal<OrganizationModel | null> = signal<OrganizationModel | null>(null);

  private userService: UserService = inject(UserService);
  private orgMemberService: OrgMemberService = inject(OrgMemberService);

  private activatedRoute: ActivatedRoute = inject(ActivatedRoute);

  private orgId: string | null = null;


  ngOnInit(): void {
    this.orgId = this.activatedRoute.snapshot.paramMap.get('orgId') ?? '';
    this.loadData();
  }

  loadData() {
    this.orgMemberService
        .getUserByOrgId(this.orgId!)
        .pipe(
            map(user => user?.userId ?? null),
            switchMap(userId => {
              if (!userId) throw new Error("Cannot find this id");

              return forkJoin({
                userId: of(userId),
                user: this.userService.getById(userId.toString())
              })
            })
        ).subscribe(result => {
          this.user.set(result.user)
    });
  }



}
