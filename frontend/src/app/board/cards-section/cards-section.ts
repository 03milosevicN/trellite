import {Component, inject, OnInit, signal, WritableSignal} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {CardModel} from "../../../models/card.model";
import {CardService} from "../../../services/card.service";
import {OrgMemberService} from "../../../services/orgMember.service";
import {ActivatedRoute} from "@angular/router";
import {forkJoin, map, of, switchMap} from "rxjs";
import {UserService} from "../../../services/user.service";
import {UserModel} from "../../../models/user.model";

@Component({
  selector: "app-cards-section",
  imports: [
    FormsModule
  ],
  templateUrl: "./cards-section.html",
  styleUrl: "./cards-section.css",
})
export class CardsSection implements OnInit {

  isEditing: WritableSignal<boolean> = signal(false);
  cardText: WritableSignal<string> = signal("+");
  cardsContainer: WritableSignal<CardModel[] | null> = signal([]);
  protected user: WritableSignal<UserModel | null> = signal<UserModel | null>(null);

  private activatedRoute: ActivatedRoute = inject(ActivatedRoute);

  draft: string = '';

  private cardService: CardService = inject(CardService);
  private orgMemberService: OrgMemberService = inject(OrgMemberService);
  private userService: UserService = inject(UserService);

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
                user: this.userService.getById(userId.toString()),
                cards: this.orgMemberService.getAllCardsByUserIdViaBoards(userId.toString()),
              });
            })
        ).subscribe({
      next: data => {
        this.user.set(data.user);
        this.cardsContainer.update(cards => (data.cards ?? []));
      },
      error: err => {
        console.error(err);
      }
    });
  }

  createCard(): void {
    this.isEditing.set(true);
  }

  save(): void {
    if (this.draft === '') return;
      const card: CardModel = {
        id: '',
        boardListId: '',
        title: this.draft,
        desc: '',
        assignees: [],
        labels: [],
        dueDate: new Date(),
        checklists: []
      };
    console.log('log: new card named: ' + card.title );
    this.isEditing.set(false);
    this.pushToContainer(card);
    this.draft = '';
  }

  pushToContainer(newCard: CardModel): void {
    this.cardsContainer.update(data => [...data!, newCard]);
    console.log(`received new card: ${newCard.title}`);
  }

}
