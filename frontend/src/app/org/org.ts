import {Component, inject, signal, WritableSignal} from "@angular/core";
import {OrganizationModel} from "../../models/organization.model";
import {OrgMemberService} from "../../services/orgMember.service";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {BoardModel} from "../../models/board.model";
import {filter, forkJoin, map} from "rxjs";
import {LucideCalendarClock, LucidePersonStanding, LucideUsers} from "@lucide/angular";
import {UpperCasePipe} from "@angular/common";
import {Header} from "../common/header/header";
import {OrganizationService} from "../../services/organization.service";
import {BoardService} from "../../services/board.service";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {Board} from "../board/board";
import {BoardRequest} from "../../models/board-request.model";
import {BoardModal} from "./board-modal/board-modal";

@Component({
  selector: "app-org",
  imports: [
    LucidePersonStanding,
    LucideCalendarClock,
    UpperCasePipe,
    RouterLink,
    Header,
    LucideUsers
  ],
  templateUrl: "./org.html",
  styleUrl: "./org.css",
})
export class Org {

  protected orgsSignal: WritableSignal<OrganizationModel[] | null> = signal<OrganizationModel[] | null>(null);
  protected boardsSignal: WritableSignal<BoardModel[] | null> = signal<BoardModel[] | null>(null);

  private orgMemberService: OrgMemberService = inject(OrgMemberService);
  private boardService: BoardService = inject(BoardService);
  private activatedRoute: ActivatedRoute = inject(ActivatedRoute);
  private orgService: OrganizationService = inject(OrganizationService);
  private router: Router = inject(Router);

  private userId: string | null = null;

  private boardModal: MatDialog = inject(MatDialog);

  protected boardRequest: BoardRequest = {
    title: '',
    createdAt: new Date(),
    archived: false,
    members: [],
  };


  constructor() {
    this.userId = this.activatedRoute.snapshot.paramMap.get('userId')!;
    this.loadData();
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
      },
      error: err => {
        console.error('Issue with loading data', err);
        this.orgsSignal.set([]);
        this.boardsSignal.set([]);
      }
    });
  }

  createBoard(): void {
    this.boardRequest.createdAt = new Date();
    this.boardService.create(this.boardRequest).subscribe({
      next: (data) => {
        this.router.navigate(['boards', data.id], {
          relativeTo: this.activatedRoute,
        });
      },
      error: err => {
        console.error(`Issue with creating a board: ${err}`);
      }
    })
  }

  openBoardModal(): void {
    const modalPointer: MatDialogRef<BoardModal> = this.boardModal.open(BoardModal, {
      width: '300px',
    });
    modalPointer.afterClosed().subscribe(result => {
      if (result !== undefined) {
        this.boardRequest.title = result
        this.createBoard();
      }
    })
  }

}
