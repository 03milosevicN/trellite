import {Component, inject, signal, WritableSignal} from "@angular/core";
import {Board as BoardModel} from "../../../models/board.model";
import {OrgMemberService} from "../../../services/orgMember.service";
import {Router} from "@angular/router";
import {BoardService} from "../../../services/board.service";
import {forkJoin, switchMap} from "rxjs";

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
          const orgId = orgMember?.organization.toString();
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
