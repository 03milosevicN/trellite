import {Component, inject,  signal, WritableSignal} from "@angular/core";
import {BoardModel} from "../../../models/board.model";
import {OrgMemberService} from "../../../services/orgMember.service";
import {ActivatedRoute, Router} from "@angular/router";
import {BoardService} from "../../../services/board.service";
import {filter, forkJoin, map, switchMap} from "rxjs";
import {flattenedColorStore} from "../../../models/color.store";

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

  private activatedRoute: ActivatedRoute = inject(ActivatedRoute);

  private routeUserId: string | null = null;


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
    const updated = this.boards()!.map(board => {

      const randColor = flattenedColorStore[Math.floor(Math.random() * flattenedColorStore.length)];

      return {
        ...board,
        color: randColor
      };
    });
    this.boards.set(updated);
  }

}
