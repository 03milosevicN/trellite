import {Component, inject, signal, WritableSignal} from "@angular/core";
import {OrganizationModel} from "../../models/organization.model";
import {OrgMemberService} from "../../services/orgMember.service";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {BoardModel} from "../../models/board.model";
import {filter, forkJoin, map} from "rxjs";
import {LucideCalendarClock, LucidePersonStanding} from "@lucide/angular";
import {UpperCasePipe} from "@angular/common";
import {Header} from "../common/header/header";
import {OrganizationService} from "../../services/organization.service";

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
  private orgService: OrganizationService = inject(OrganizationService);

  private userId: string | null = null;

  constructor() {
    this.userId = this.activatedRoute.snapshot.paramMap.get('userId')!;
    this.loadData();
  }

  loadData2() {
    this.orgService.getByOwner(this.userId!).subscribe({
      next: data => {
        this.orgsSignal.set(data);
      }
    })
  }

  // constructor() {
  //   this.activatedRoute.parent?.paramMap.pipe(
  //       map(params => params.get('userId')),
  //       filter( (id) : id is string => id !== null )
  //   ).subscribe(id => {
  //     this.routeUserId = id;
  //     console.log(id);
  //     this.loadData();
  //   });
  // }

  loadDataProto() {
    this.orgService.getByOwner(this.userId!).subscribe({
      next: data => {
        this.orgsSignal.update(() => [...data]);
      }
    })
  }

  loadData(): void {
    this.orgsSignal.set(null);
    this.boardsSignal.set(null);

    forkJoin({
      orgs: this.orgService.getByOwner(this.userId!),
      boards: this.orgMemberService.getAllBoardsByUserId(this.userId!),
    }).subscribe({
      next: ({orgs, boards}) => {
        this.orgsSignal.set(orgs || []);
        this.boardsSignal.set(boards || []);
        console.log('orgs: ' + this.orgsSignal()?.at(0)?.name);
        console.log('boards: ' + this.boardsSignal()?.at(0)?.title);
      },
      error: err => {
        console.error('Issue with loading data', err);
        this.orgsSignal.set([]);
        this.boardsSignal.set([]);
      }
    });
  }

}
