import {Component, inject, OnInit, signal, WritableSignal} from "@angular/core";
import {UserModel} from "../../../models/user.model";
import {OrganizationModel} from "../../../models/organization.model";
import {UserService} from "../../../services/user.service";
import {ActivatedRoute, ActivatedRouteSnapshot, Router} from "@angular/router";
import {NgOptimizedImage, UpperCasePipe} from "@angular/common";
import {OrganizationService} from "../../../services/organization.service";

@Component({
  selector: "app-header",
  imports: [
    NgOptimizedImage,
    UpperCasePipe
  ],
  templateUrl: "./header.html",
  styleUrl: "./header.css",
})
export class Header implements OnInit {

  protected user: WritableSignal<UserModel | null> = signal<UserModel | null>(null);
  protected org: WritableSignal<OrganizationModel | null> = signal<OrganizationModel | null>(null);

  private userService: UserService = inject(UserService);
  private organizationService: OrganizationService = inject(OrganizationService);

  private activatedRoute: ActivatedRoute = inject(ActivatedRoute);

  private orgId: string | null = null;

  ngOnInit(): void {
    this.orgId = this.activatedRoute.snapshot.paramMap.get('orgId') ?? '';
    this.loadData();
  }

  loadDataProto(): void {
    this.organizationService.getById(this.orgId!).subscribe({
      next: data => {
        console.log(data);
      }
    })
  }


  loadData(): void {
    this.userService.getById(this.orgId!).subscribe({
      next: data => {
        this.user.set(data);
      },
      error: err => {
        console.error(err);
      }
    })
  }

}
