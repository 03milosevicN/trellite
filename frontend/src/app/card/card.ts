import { Component, computed, input, output } from '@angular/core';
import { CardResponseModel } from '../board/board.model';
import { LucideClock, LucideInfo, LucideSquareCheckBig, LucideTrash2 } from '@lucide/angular';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-card',
  imports: [LucideSquareCheckBig, DatePipe, LucideClock, LucideInfo, LucideTrash2],
  template: `
    <div
      class="card bg-base-100 border border-base-200 shadow-xs hover:border-primary/50 transition-all hover:shadow-sm cursor-grab active:cursor-grabbing group p-3 gap-2"
    >
      <!-- Labels Section -->
      @if (card().labels && card().labels!.length > 0) {
        <div class="flex flex-wrap gap-1">
          @for (label of card().labels; track label) {
            <span class="badge badge-xs font-semibold badge-primary badge-soft">{{ label }}</span>
          }
        </div>
      }

      <!-- Header: Title & Action SVGs -->
      <div class="flex items-start justify-between gap-2">
        <h3 class="card-title text-xs font-semibold leading-snug text-base-content">
          {{ card().title }}
        </h3>

        <!-- Action SVG Buttons -->
        <div
          class="flex items-center gap-1 shrink-0 opacity-80 group-hover:opacity-100 transition-opacity"
        >
          <!-- Details Button -->
          <button
            type="button"
            class="btn btn-ghost btn-xs btn-square text-base-content/60 hover:text-primary transition-colors"
            title="Card Details"
            (click)="$event.stopPropagation(); openDetails.emit(card())"
          >
            <svg lucideInfo class="w-3.5 h-3.5"></svg>
          </button>

          <!-- Delete Button -->
          <button
            type="button"
            class="btn btn-ghost btn-xs btn-square text-base-content/60 hover:text-error transition-colors"
            title="Delete Card"
            (click)="$event.stopPropagation(); openDelete.emit(card())"
          >
            <svg lucideTrash2 class="w-3.5 h-3.5"></svg>
          </button>
        </div>
      </div>

      <!-- Description Preview -->
      @if (card().desc) {
        <p class="text-xs text-base-content/70 leading-normal line-clamp-2">
          {{ card().desc }}
        </p>
      }

      <!-- Metadata Footer: Due Date, Checklists, Assignees -->
      @if (hasFooterMetadata()) {
        <div
          class="flex items-center justify-between gap-2 pt-1.5 border-t border-base-200/60 text-[11px] text-base-content/60"
        >
          <div class="flex items-center gap-2 flex-wrap">
            <!-- Due Date Badge -->
            @if (card().dueDate) {
              <div
                class="flex items-center gap-1 px-1.5 py-0.5 rounded font-medium"
                [class.bg-error]="isPastDue(card().dueDate)"
                [class.text-error]="isPastDue(card().dueDate)"
                [class.bg-base-200]="!isPastDue(card().dueDate)"
              >
                <svg lucideClock class="w-3 h-3"></svg>
                <span>{{ card().dueDate | date: 'shortDate' }}</span>
              </div>
            }

            <!-- Checklist Progress -->
            @if (card().checklists && card().checklists!.length > 0) {
              <div class="flex items-center gap-1 bg-base-200 px-1.5 py-0.5 rounded font-medium">
                <svg lucideCheckSquare class="w-3 h-3"></svg>
                <span>{{ completedChecklistItemsCount() }}/{{ totalChecklistItemsCount() }}</span>
              </div>
            }
          </div>

          <!-- Assignees Avatars -->
          @if (card().assignees && card().assignees.length > 0) {
            <div class="avatar-group -space-x-2 rtl:space-x-reverse shrink-0">
              @for (assigneeId of card().assignees.slice(0, 3); track assigneeId) {
                <div class="avatar placeholder">
                  <div
                    class="w-4 h-4 rounded-full bg-neutral text-neutral-content flex items-center justify-center text-[9px] font-bold"
                  >
                    <span>{{ assigneeId }}</span>
                  </div>
                </div>
              }
              @if (card().assignees.length > 3) {
                <div class="avatar placeholder">
                  <div
                    class="w-4 h-4 rounded-full bg-base-300 text-base-content flex items-center justify-center text-[8px] font-bold"
                  >
                    <span>+{{ card().assignees.length - 3 }}</span>
                  </div>
                </div>
              }
            </div>
          }
        </div>
      }
    </div>
  `,
})
export class Card {
  card = input.required<CardResponseModel>();

  openDetails = output<CardResponseModel>();
  openDelete = output<CardResponseModel>();

  protected hasFooterMetadata = computed(() => {
    const cardPointer = this.card();
    return !!(
      cardPointer.dueDate ||
      (cardPointer.checklists && cardPointer.checklists.length > 0) ||
      (cardPointer.assignees && cardPointer.assignees.length > 0)
    );
  });

  protected totalChecklistItemsCount = computed(() => {
    return this.card().checklists?.reduce((acc, c) => acc + (c.items?.length || 0), 0) ?? 0;
  });

  protected completedChecklistItemsCount = computed(() => {
    return (
      this.card().checklists?.reduce(
        (acc, c) => acc + (c.items?.filter((item) => item.id)?.length || 0),
        0,
      ) ?? 0
    );
  });

  protected isPastDue(dueDate?: Date) {
    if (!dueDate) return false;
    return new Date(dueDate).getTime() < Date.now();
  }
}
