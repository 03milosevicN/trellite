import {Component, input, signal, WritableSignal} from "@angular/core";
import {
  MatCard,
  MatCardContent,
  MatCardHeader,
  MatCardSubtitle,
  MatCardTitle,
  MatCardTitleGroup
} from "@angular/material/card";
import {CardModel} from "../../../models/card.model";

@Component({
  selector: "app-card",
  imports: [
    MatCard,
    MatCardHeader,
    MatCardTitle,
    MatCardContent,
    MatCardSubtitle,
    MatCardTitleGroup
  ],
  template: `
    <mat-card class="p-2 shadow-sm border-0 rounded-2 bg-light">

      <mat-card-header class="p-1">
        <mat-card-title-group class="w-100">

          <mat-card-title class="h6 mb-0 fw-semibold text-truncate">
            {{ cardSignal()?.title }}
          </mat-card-title>

          <mat-card-subtitle class="small text-muted">
            {{ cardSignal()?.dueDate }}
          </mat-card-subtitle>

        </mat-card-title-group>
      </mat-card-header>

      <mat-card-content class="p-1 pt-2">

        <p class="mb-1 small text-body">
          {{ cardSignal()?.desc }}
        </p>

        <p class="mb-2 small text-secondary">
          {{ cardSignal()?.labels }}
        </p>

        @for (checklist of cardSignal()?.checklists; track checklist.id) {
          <div class="mb-2">

            <p class="mb-1 small fw-semibold">
              {{ checklist.title }}
            </p>

            <ul class="list-unstyled ms-2 mb-1 small">
              @for (item of checklist.items; track item.id) {
                <li class="d-flex align-items-start gap-1">
                  <span class="text-muted">•</span>
                  <span>{{ item.task }}</span>
                </li>
              }
            </ul>

          </div>
        }

      </mat-card-content>

    </mat-card>
  `,
  styleUrl: "./card.css",
})
export class Card {
  public cardSignal = input<CardModel | null>(null);
}
