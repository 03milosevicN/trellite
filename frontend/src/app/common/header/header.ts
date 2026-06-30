import {Component, effect, inject, OnInit, signal, WritableSignal} from "@angular/core";
import {UserModel} from "../../../models/user.model";
import {OrganizationModel} from "../../../models/organization.model";
import {UserService} from "../../../services/user.service";
import {ActivatedRoute, ActivatedRouteSnapshot, Router, RouterLink} from "@angular/router";
import {NgOptimizedImage, UpperCasePipe} from "@angular/common";
import {OrganizationService} from "../../../services/organization.service";
import {OrgMemberService} from "../../../services/orgMember.service";
import {forkJoin, map, of, switchMap} from "rxjs";
import {AuthService} from "../../../services/auth.service";

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
export class Header {

  protected userSignal: WritableSignal<UserModel | null> = signal<UserModel | null>(null);
  protected org: WritableSignal<OrganizationModel | null> = signal<OrganizationModel | null>(null);

  private userService: UserService = inject(UserService);
  private authService: AuthService = inject(AuthService);


  constructor() {
    effect(() => {
      const userId = this.authService.userIdPointer();

      if (!userId) return;

      this.userService.getById(userId.toString()).subscribe({
        next: (data) => {
          this.userSignal.set(data);
          console.log(`FROM header.ts: fetched data: ${data.userId}`);
          console.log(this.userSignal());
        },
        error: err => {
          console.error(`Failed to load user: ${err.message}`);
        }
      })

    });
  }

}
