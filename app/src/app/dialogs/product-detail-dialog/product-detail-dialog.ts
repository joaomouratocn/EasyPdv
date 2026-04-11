import { CurrencyPipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';

@Component({
  selector: 'app-product-detail-dialog',
  imports: [MatDialogModule],
  templateUrl: './product-detail-dialog.html',
  styleUrl: './product-detail-dialog.css',
})
export class ProductDetailDialog {
  public data = inject(MAT_DIALOG_DATA);

  productDtoReceived = this.data.product;
}
