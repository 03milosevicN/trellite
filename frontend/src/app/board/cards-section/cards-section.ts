import {Component, inject, OnInit, signal, WritableSignal} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {CardModel} from "../../../models/card.model";
import {CardService} from "../../../services/card.service";
import {OrgMemberService} from "../../../services/orgMember.service";

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

  draft: string = '';

  private cardService: CardService = inject(CardService);
  private orgMemberService: OrgMemberService = inject(OrgMemberService);


  ngOnInit(): void {

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
