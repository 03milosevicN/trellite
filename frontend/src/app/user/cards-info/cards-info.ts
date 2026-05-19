import {Component, inject, signal, WritableSignal} from "@angular/core";
import {Router} from "@angular/router";
import {CardModel} from "../../../models/card.model";
import {OrgMemberService} from "../../../services/orgMember.service";
import {DatePipe} from "@angular/common";

@Component({
  selector: "app-cards-info",
  imports: [
    DatePipe
  ],
  templateUrl: "./cards-info.html",
  styleUrl: "./cards-info.css",
})
export class CardsInfo {

  protected cards: WritableSignal<CardModel[] | null> = signal<CardModel[] | null>(null);

  private orgMemberService: OrgMemberService = inject(OrgMemberService);

  private router: Router = inject(Router);
  private routeUserId: string | null = null;


  constructor() {
    this.routeUserId = this.router.url[3];
    this.loadData();
  }


  loadData(): void {
    this.orgMemberService.getAllCardsByUserIdViaBoards(this.routeUserId!).subscribe({
      next: data => {
        this.cards.update(() => [...data!]);
      },
      error: err => {
        console.log(err);
      }
    });
  }

}
