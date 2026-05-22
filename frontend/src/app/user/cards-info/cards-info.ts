import {Component, computed, inject, OnInit, Signal, signal, WritableSignal} from "@angular/core";
import {Router} from "@angular/router";
import {CardModel} from "../../../models/card.model";
import {OrgMemberService} from "../../../services/orgMember.service";
import {DatePipe} from "@angular/common";
import {BoardModel} from "../../../models/board.model";
import {BoardListModel} from "../../../models/boardList.model";
import {forkJoin} from "rxjs";

type AccumulatorTemplate = {
  id: string;
  cardTitle: string;
  boardListTitle: string;
  boardTitle: string;
  labels: string[];
  due: Date;
};

@Component({
  selector: "app-cards-info",
  imports: [
    DatePipe
  ],
  templateUrl: "./cards-info.html",
  styleUrl: "./cards-info.css",
})
export class CardsInfo implements OnInit {

  protected cards: WritableSignal<CardModel[] | null> = signal<CardModel[] | null>(null);
  protected boards: WritableSignal<BoardModel[] | null> = signal<BoardModel[] | null>(null);
  protected boardLists: WritableSignal<BoardListModel[] | null> = signal<BoardListModel[] | null>(null);

  protected accumulator: Signal<AccumulatorTemplate[]> = computed(() => {
    const cards: CardModel[] = this.cards() ?? [];
    const boards: BoardModel[] = this.boards() ?? [];
    const boardLists: BoardListModel[] = this.boardLists() ?? [];

    return cards.map(card => {
      const boardList: BoardListModel | undefined = boardLists.find(
          list => list.id === card.boardListId
      );
      const board: BoardModel | undefined = boards.find(
          b => b.id === boardList?.boardId
      );
      return {
        id: card.id,
        cardTitle: card.title ?? '',
        boardListTitle: boardList?.title ?? '',
        boardTitle: board?.title ?? '',
        labels: card.labels ?? [],
        due: card?.dueDate
      }
    });
  });

  private orgMemberService: OrgMemberService = inject(OrgMemberService);

  private router: Router = inject(Router);
  private routeUserId: string | null = null;


  ngOnInit(): void {
    this.routeUserId = this.router.url[3];
    this.loadData();
  }


  loadData(): void {
    forkJoin({
      cards: this.orgMemberService.getAllCardsByUserIdViaBoards(this.routeUserId!),
      boards: this.orgMemberService.getAllBoardsByUserId(this.routeUserId!),
      boardLists: this.orgMemberService.getAllBoardListsByUserId(this.routeUserId!)
    }).subscribe({
      next: ({cards, boards, boardLists}) => {
        this.cards.set(cards);
        this.boards.set(boards);
        this.boardLists.set(boardLists);
      },
      error: error => {
        console.log('Issue with loading data: ', error);
      }
    })
  }

}
