import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { CategooryDialog } from '../categoory-dialog/categoory-dialog';

@Component({
  selector: 'app-measure-dialog',
  imports: [MatDialogModule, FormsModule],
  templateUrl: './measure-dialog.html',
  styleUrl: './measure-dialog.css',
})
export class MeasureDialog {
  public data = inject(MAT_DIALOG_DATA);
  private dialogRef = inject(MatDialogRef<CategooryDialog>);

  measureName = this.data.categoryName;

  cancel() {
    this.dialogRef.close();
  }
}
