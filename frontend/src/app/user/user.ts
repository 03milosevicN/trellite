import {Component, inject, OnInit, signal, WritableSignal} from "@angular/core";
import {UserService} from "../../services/user.service";
import {ActivatedRoute, Router, RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";
import {UserModel} from "../../models/user.model";
import {
  LucideCog,
  LucideLogOut,
  LucideUsers,
} from "@lucide/angular";
import {OrganizationModel} from "../../models/organization.model";
import {AuthService} from "../../services/auth.service";
import {OrganizationService} from "../../services/organization.service";
import {FormsModule} from "@angular/forms";
import {OrganizationRequestModel} from "../../models/organizationRequest.model";
import {ModalDialog} from "./modal-dialog/modalDialog";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {MatButton} from "@angular/material/button";

@Component({
  selector: "app-user",
  imports: [
    LucideCog,
    LucideUsers,
    RouterLink,
    RouterOutlet,
    RouterLinkActive,
    LucideLogOut,
    FormsModule,
    MatButton
  ],
  templateUrl: "./user.html",
  styleUrl: "./user.css",
})
export class User implements OnInit {


  protected userSignal: WritableSignal<UserModel | null> = signal<UserModel | null>(null);

  protected orgsSignal: WritableSignal<OrganizationModel[] | null> = signal<OrganizationModel[] | null>(null);

  protected userId: string = '';

  private userService: UserService = inject(UserService);
  protected authService: AuthService = inject(AuthService);
  protected orgService: OrganizationService = inject(OrganizationService);
  private activatedRoute: ActivatedRoute = inject(ActivatedRoute);
  private router: Router = inject(Router);

  protected orgRequest: OrganizationRequestModel = {
    name: '',
    createdAt: new Date(),
  };

  private dialog: MatDialog = inject(MatDialog);


  ngOnInit(): void {
    this.userId = this.activatedRoute.snapshot.paramMap.get('userId')!;
    console.log(`Parsed userId type: ${typeof this.userId}`);
    this.loadData();
  }


  loadData(): void {
    this.orgService.getByOwner(this.userId).subscribe({
      next: data => {
        this.orgsSignal.update(() => [...data]);
        console.log(this.orgsSignal()?.length);
      },
      error: err => {
        console.error('Error accessing user orgs: ' + err);
      }
    })
  }

  createOrg(): void {
    this.orgRequest.createdAt = new Date();
    this.orgService.create(this.orgRequest).subscribe({
      next: (data) => {
        console.log(`Created org: ${data}`);
        this.router.navigate([`/orgs/${data.orgId}`]);
      },
      error: err => {
        console.error('Error creating org: ' + err);
      }
    });
  }

  openOrgModal(): void {
    const dialogPointer = this.dialog.open(ModalDialog, {
      width: '300px'
    });
    dialogPointer.afterClosed().subscribe(result => {
      if (result !== undefined) {
        this.orgRequest.name = result;
        this.createOrg();
      }
    });
  }

}
