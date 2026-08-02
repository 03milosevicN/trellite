import {Component, inject, signal, WritableSignal} from "@angular/core";
import {
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";
import {MatFormField, MatInput, MatLabel} from "@angular/material/input";
import {MatButton} from "@angular/material/button";
import {FormsModule} from "@angular/forms";

@Component({
  selector: "app-board-modal",
  imports: [
    MatDialogTitle,
    MatFormField,
    MatLabel,
    MatInput,
    MatDialogActions,
    MatButton,
    FormsModule,
    MatDialogContent
  ],
  template: `
    <h2 mat-dialog-title>Board menu</h2>
    <mat-dialog-content>
      <mat-form-field>
        <mat-label>Board name</mat-label>
        <input matInput [(ngModel)]="boardName" placeholder="Board name...">
      </mat-form-field>
      <mat-dialog-actions>
        <button mat-button (click)="onCancel()">Cancel</button>
        <button mat-button (click)="onSave()">Create</button>
      </mat-dialog-actions>
    </mat-dialog-content>
  `,
})
export class BoardModal {
  modalPointer = inject(MatDialogRef<BoardModal>);
  boardName: WritableSignal<string> = signal('');

  protected onCancel(): void {
    this.modalPointer.close();
  }

  protected onSave(): void {
    this.modalPointer.close(this.boardName());
  }
}
