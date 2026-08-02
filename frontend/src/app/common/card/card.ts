import {Component, inject, input, output, signal, WritableSignal} from "@angular/core";
import {
  MatCard,
  MatCardContent,
  MatCardHeader,
  MatCardSubtitle,
  MatCardTitle,
  MatCardTitleGroup
} from "@angular/material/card";
import {CardModel} from "../../../models/card.model";
import {DatePipe} from "@angular/common";
import {CardEditDialog} from "../card-edit-modal/card-edit-modal";
import {MatDialog} from "@angular/material/dialog";
import {CardService} from "../../../services/card.service";

@Component({
  selector: "app-card",
  imports: [
    MatCard,
    MatCardHeader,
    MatCardTitle,
    MatCardContent,
    MatCardSubtitle,
    MatCardTitleGroup,
    DatePipe
  ],
  template: `
    <mat-card (click)="openEditDialog()"
              class="position-relative p-3 border border-primary-subtle rounded-3 shadow-sm bg-primary-subtle bg-opacity-25"
              style="cursor: pointer;">
      <mat-card-header class="p-0 mb-3 me-3"> <mat-card-title-group class="w-100 d-flex flex-column gap-1">
        <mat-card-title class="h5 mb-0 fw-bold text-primary text-truncate">
          {{ cardSignal()?.title }}
        </mat-card-title>
        <mat-card-subtitle class="mt-1">
            <span class="badge bg-white text-primary border border-primary-subtle fw-medium px-2 py-1">
              {{ cardSignal()?.dueDate | date:'mediumDate' }}
            </span>
        </mat-card-subtitle>
      </mat-card-title-group>
      </mat-card-header>

      <mat-card-content class="p-0">
        <p class="mb-2 text-dark lh-sm">{{ cardSignal()?.desc }}</p>
      </mat-card-content>
    </mat-card>
  `,
})
export class Card {
  public cardSignal = input<CardModel | null>(null);
  public cardDeleted = output<string>();
  private dialog = inject(MatDialog);
  private cardService: CardService = inject(CardService);


  openEditDialog(): void {
    const currentCard = this.cardSignal();
    if (!currentCard) return;

    const dialogRef = this.dialog.open(CardEditDialog, {
      data: { card: currentCard },
      autoFocus: 'first-tabbable'
    });

    dialogRef.afterClosed().subscribe((updatedCard: CardModel | undefined) => {
      if (updatedCard) {
        console.log('Card data updated to save:', updatedCard);

        this.cardService.update(currentCard.id, updatedCard).subscribe({
          next: (data) => {
            console.log('Card data updated to save:', data);
          }
        });
      }
    });
  }

}
