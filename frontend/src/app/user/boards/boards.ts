import {Component, ElementRef, inject, Signal, signal, viewChild, WritableSignal} from "@angular/core";
import {BoardModel} from "../../../models/board.model";
import {OrgMemberService} from "../../../services/orgMember.service";
import {Router} from "@angular/router";
import {BoardService} from "../../../services/board.service";
import {forkJoin, switchMap} from "rxjs";
import {BoardListService} from "../../../services/board-list.service";
import {DatePipe} from "@angular/common";

@Component({
  selector: "app-boards",
  imports: [
  ],
  templateUrl: "./boards.html",
  styleUrl: "./boards.css",
})
export class Boards {

  protected boards: WritableSignal<BoardModel[] | null> = signal<BoardModel[] | null>(null);

  private orgMemberService: OrgMemberService = inject(OrgMemberService);
  private boardService: BoardService = inject(BoardService);

  private router: Router = inject(Router);

  private routeUserId: string | null = null;

  protected boardCardDiv = signal('');


  constructor() {
    this.routeUserId = this.router.url[3];
    this.loadData();
  }


  loadData(): void {
    this.orgMemberService.getByUserId(this.routeUserId!).pipe(
        switchMap(orgMember => {
          const orgId = orgMember?.orgId.toString();
          return forkJoin({
            boards: this.boardService.getAllByOrgId(orgId!)
          });
        })
    ).subscribe({
      next: (data) => {
        this.boards.update( () => [...data.boards] );
        this.boardCardColorPicker();
      },
      error: err => {
        console.error('Error loading data', err);
      }
    })
  }

  boardCardColorPicker(): void {
    this.boardCardDiv.set('#32a852');
  }

}
