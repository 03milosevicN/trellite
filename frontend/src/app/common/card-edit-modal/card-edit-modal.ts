import { Component, Inject, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { FormsModule } from '@angular/forms';
import {CardModel} from "../../../models/card.model";

@Component({
    selector: 'app-card-edit-dialog',
    standalone: true,
    imports: [MatDialogModule, FormsModule],
    template: `
    <div class="p-4" style="min-width: 400px;">
      <h2 mat-dialog-title class="h4 border-bottom pb-2 mb-3">Edit Card</h2>
      
      <mat-dialog-content>
        <div class="mb-3">
          <label class="form-label small fw-bold">Title</label>
          <input [(ngModel)]="localCard.title" type="text" class="form-control" />
        </div>

        <div class="mb-3">
          <label class="form-label small fw-bold">Description</label>
          <textarea [(ngModel)]="localCard.desc" class="form-control" rows="3"></textarea>
        </div>
      </mat-dialog-content>

      <mat-dialog-actions class="d-flex justify-content-end gap-2 pt-2 border-top mt-3">
        <button class="btn btn-sm btn-outline-secondary" (click)="onCancel()">Cancel</button>
        <button class="btn btn-sm btn-primary px-3" (click)="onSave()">Save</button>
      </mat-dialog-actions>
    </div>
  `
})
export class CardEditDialog {
    private dialogRef = inject(MatDialogRef<CardEditDialog>);
    public data = inject<{ card: CardModel }>(MAT_DIALOG_DATA);
    public localCard: CardModel = JSON.parse(JSON.stringify(this.data.card));

    onCancel(): void {
        this.dialogRef.close();
    }

    onSave(): void {
        this.dialogRef.close(this.localCard);
    }
}