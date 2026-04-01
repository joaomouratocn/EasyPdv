import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-categoory-dialog',
  imports: [MatDialogModule, FormsModule],
  templateUrl: './categoory-dialog.html',
  styleUrl: './categoory-dialog.css',
})
export class CategooryDialog {
  public data = inject(MAT_DIALOG_DATA);
  private dialogRef = inject(MatDialogRef<CategooryDialog>);

  categoryName = this.data.categoryName;

  cancel() {
    this.dialogRef.close();
  }
}
