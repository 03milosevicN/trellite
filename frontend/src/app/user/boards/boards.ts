import {Component, inject, signal, WritableSignal} from "@angular/core";
import {BoardModel} from "../../../models/board.model";
import {OrgMemberService} from "../../../services/orgMember.service";
import {Router} from "@angular/router";
import {BoardService} from "../../../services/board.service";
import {forkJoin, switchMap} from "rxjs";
import {BoardListService} from "../../../services/board-list.service";

@Component({
  selector: "app-boards",
  imports: [],
  templateUrl: "./boards.html",
  styleUrl: "./boards.css",
})
export class Boards {

  protected boards: WritableSignal<BoardModel[] | null> = signal<BoardModel[] | null>(null);

  private orgMemberService: OrgMemberService = inject(OrgMemberService);
  private boardService: BoardService = inject(BoardService);
  private boardListService: BoardListService = inject(BoardListService);

  private router: Router = inject(Router);

  private routeUserId: string | null = null;

  constructor() {
    this.routeUserId = this.router.url[3];
    this.loadData();
  }

  //! Might be too intense for the frontend.
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
      },
      error: err => {
        console.error('Error loading data', err);
      }
    })
  }
  //! Might be too intense for the frontend.


}
