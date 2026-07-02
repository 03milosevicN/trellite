import {Component, inject, OnInit, signal, WritableSignal} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {OrgMemberService} from "../../../services/orgMember.service";
import {ActivatedRoute} from "@angular/router";
import {forkJoin, map, of, switchMap} from "rxjs";
import {BoardListModel} from "../../../models/boardList.model";
import {BoardListService} from "../../../services/board-list.service";
import {BoardList} from "../../common/board-list/board-list";

@Component({
  selector: "app-lists-section",
  imports: [
    FormsModule,
    BoardList
  ],
  templateUrl: "./lists-section.html",
  styleUrl: "./lists-section.css",
})
export class ListsSection implements OnInit {

  isEditing: WritableSignal<boolean> = signal(false);
  listText: WritableSignal<string> = signal("+");
  listsContainer: WritableSignal<BoardListModel[] | null> = signal([]);

  private activatedRoute: ActivatedRoute = inject(ActivatedRoute);
  private orgMemberService: OrgMemberService = inject(OrgMemberService);
  private boardListService: BoardListService = inject(BoardListService);

  draft: string = "";
  private orgId: string | null = null;



  ngOnInit(): void {
    this.orgId = this.activatedRoute.snapshot.paramMap.get("orgId") ?? '';
    this.loadData();
  }


  loadData(): void {
    this.orgMemberService
        .getUserByOrgId(this.orgId!)
        .pipe(
            map(user => user?.userId ?? null),
            switchMap(userId => {
              if (!userId) throw new Error("Cannot find user with id of" + userId);
              return forkJoin({
                userId: of(userId),
                boardLists: this.orgMemberService.getAllBoardListsByUserId(userId.toString())
              });
            })
        )
        .subscribe({
          next: data => {
            this.listsContainer.update(lists => (data.boardLists ?? []));
          },
          error: err => {
            console.error(err);
          }
    });
  }

  protected createBoard(): void {
    this.isEditing.set(true);
  }

  save(): void {
    if (this.draft === '') return;
    const list: BoardListModel = {
      id: '',
      boardId: '',
      title: this.draft,
      createdAt: new Date()
    }
    this.isEditing.set(false);
    this.pushToContainer(list);
    this.boardListService.create(list).subscribe({
      next: (data) => {
        console.log(`Log: created board: ${data.title}`);
      },
      error: err => {
        console.log(`Error creating board: ${err}`);
      }
    })
    this.draft = '';
  }

  pushToContainer(newBoard: BoardListModel): void {
    this.listsContainer.update(data => [...data!, newBoard]);
  }

}
