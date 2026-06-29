import {Component, inject, signal, WritableSignal} from "@angular/core";
import {OrganizationModel} from "../../models/organization.model";
import {OrgMemberService} from "../../services/orgMember.service";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {BoardModel} from "../../models/board.model";
import {filter, forkJoin, map} from "rxjs";
import {LucideCalendarClock, LucidePersonStanding} from "@lucide/angular";
import {UpperCasePipe} from "@angular/common";
import {Header} from "../common/header/header";

@Component({
  selector: "app-org",
  imports: [
    LucidePersonStanding,
    LucideCalendarClock,
    UpperCasePipe,
    RouterLink,
    Header
  ],
  templateUrl: "./org.html",
  styleUrl: "./org.css",
})
export class Org {

  protected orgsSignal: WritableSignal<OrganizationModel[] | null> = signal<OrganizationModel[] | null>(null);
  protected boardsSignal: WritableSignal<BoardModel[] | null> = signal<BoardModel[] | null>(null);

  private orgMemberService:OrgMemberService = inject(OrgMemberService);

  private activatedRoute: ActivatedRoute = inject(ActivatedRoute);

  protected routeUserId: string = '';


  constructor() {
    this.activatedRoute.parent?.paramMap.pipe(
        map(params => params.get('userId')),
        filter( (id) : id is string => id !== null )
    ).subscribe(id => {
      this.routeUserId = id;
      this.loadData();
    });
  }


  loadData(): void {
    forkJoin({
      orgs: this.orgMemberService.getAllOrgsByUserId(this.routeUserId!),
      boards: this.orgMemberService.getAllBoardsByUserId(this.routeUserId!),
    }).subscribe({
      next: ({orgs, boards}) => {
        this.orgsSignal.update( () => [...orgs]);
        this.boardsSignal.update( () => [...boards!]);
      },
      error: err => {
        console.error('Issue with loading data', err);
      }
    });
  }

}
