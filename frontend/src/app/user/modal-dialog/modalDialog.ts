import {Component, inject, signal, WritableSignal} from "@angular/core";
import {MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle} from "@angular/material/dialog";
import {MatFormField, MatInput, MatLabel} from "@angular/material/input";
import {FormsModule} from "@angular/forms";
import {MatButton} from "@angular/material/button";

@Component({
    selector: "app-modal-dialog",
    template: `
        <h2 mat-dialog-title>Organization menu</h2>
        <mat-dialog-content>
            <mat-form-field>
                <mat-label>Name</mat-label>
                <input matInput [(ngModel)]="orgName" placeholder="Your organization's name..."/>
            </mat-form-field>
        </mat-dialog-content>
        <mat-dialog-actions>
            <button mat-button (click)="onCancel()">Cancel</button>
            <button mat-button (click)="onSave()">Create</button>
        </mat-dialog-actions>
    `,
    imports: [
        MatDialogTitle,
        MatDialogContent,
        MatFormField,
        MatLabel,
        MatInput,
        FormsModule,
        MatDialogActions,
        MatButton
    ]
})
export class ModalDialog {
    dialogPointer = inject(MatDialogRef<ModalDialog>);
    orgName: WritableSignal<string> = signal('');

    onCancel(): void {
        this.dialogPointer.close();
    }

    onSave(): void {
        this.dialogPointer.close(this.orgName());
    }
}