import {Component, input, signal, WritableSignal} from "@angular/core";
import {BoardListModel} from "../../../models/boardList.model";
import {MatCard, MatCardContent, MatCardTitle, MatCardTitleGroup} from "@angular/material/card";
import {CdkDropList, CdkDrag, CdkDragDrop} from "@angular/cdk/drag-drop";
import {Card} from "../card/card";
import {CardModel} from "../../../models/card.model";

@Component({
  selector: "app-board-list",
  imports: [
    MatCard,
    MatCardContent,
    MatCardTitleGroup,
    MatCardTitle,
    Card,
    CdkDropList,
    CdkDrag
  ],
  template: `
    <div class="board-list-wrapper">
      <mat-card appearance="outlined" class="shadow border border-secondary-subtle rounded-3 h-100 p-2">
        <mat-card-title-group class="mb-3">
          <mat-card-title class="fs-5 fw-bold text-dark">{{ boardListSignal()?.title }}</mat-card-title>
        </mat-card-title-group>

        <mat-card-content
            cdkDropList
            [cdkDropListData]="listCards()"
            (cdkDropListDropped)="drop($event)"
            class="d-flex flex-column gap-2 min-vh-25"
            style="min-height: 100px;">

          @for (card of listCards(); track card.id) {
            <app-card [cardSignal]="card" cdkDrag [cdkDragData]="card"></app-card>
          }

        </mat-card-content>
      </mat-card>
    </div>
  `,
  styleUrl: "./board-list.css",
})
export class BoardList {

  boardListSignal = input<BoardListModel | null>(null);

  listCards = signal<CardModel[]>([]);

  drop(event: CdkDragDrop<CardModel[]>): void {
    if (event.previousContainer === event.container) {

      const updated = [...event.container.data];
      const [moved] = updated.splice(event.previousIndex, 1);
      updated.splice(event.currentIndex, 0, moved);
      this.listCards.set(updated);
    } else {

      const cardDropped: CardModel = event.item.data;

      cardDropped.boardListId = this.boardListSignal()?.id ?? '';

      const updatedTarget = [...event.container.data];
      updatedTarget.splice(event.currentIndex, 0, cardDropped);
      this.listCards.set(updatedTarget);

    }
  }


}