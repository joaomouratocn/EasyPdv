import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../services/product-service';
import { ProductDto } from '../../models/dtos/product-dto';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-create-edit-product-component',
  imports: [],
  templateUrl: './create-edit-product-component.html',
  styleUrl: './create-edit-product-component.css',
})
export class CreateEditProductComponent {
  private router = inject(ActivatedRoute);
  private snackBar = inject(MatSnackBar);
  private productSerive = inject(ProductService);
  loading = signal(false);

  ngOnInit() {
    const id = this.router.snapshot.paramMap.get('id');

    if (id) {
      this.loading.set(true);
      this.productSerive.getProductById(id).subscribe({
        next: (result) => {
          this.loadForm(result);
        },
        error: (err) => {
          this.snackBar.open(err.error.message, 'X', {
            panelClass: ['error-snackbar'],
          });
        },
        complete: () => {
          this.loading.set(false);
        },
      });
    }
  }

  loadForm(product: ProductDto) {
    console.log(product);
  }
}
