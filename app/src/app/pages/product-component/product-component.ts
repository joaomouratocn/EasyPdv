import { Component, computed, inject, model, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ConfirmDeleteDialog } from '../../dialogs/confirm-delete-dialog/confirm-delete-dialog';
import { ProductDetailDialog } from '../../dialogs/product-detail-dialog/product-detail-dialog';
import { ProductDto } from '../../models/dtos/product-dto';
import { ProductService } from '../../services/product-service';

@Component({
  selector: 'app-product-component',
  imports: [FormsModule, MatProgressSpinnerModule, RouterLink],
  templateUrl: './product-component.html',
  styleUrl: './product-component.css',
})
export class ProductComponent {
  private actRouter = inject(ActivatedRoute);
  private productsService = inject(ProductService);
  private snackBar = inject(MatSnackBar);
  private dialog = inject(MatDialog);
  private router = inject(Router);

  productName = model('');
  loading = signal(false);

  products = signal<ProductDto[]>([]);

  filteredProducts = computed(() => {
    return this.products()
      .filter((product) => product.name.toLowerCase().includes(this.productName().toLowerCase()))
      .sort((a, b) => a.name.localeCompare(b.name));
  });

  ngOnInit() {
    this.loadProducts();
  }

  loadProducts() {
    this.loading.set(true);
    this.productsService.getAllProducts().subscribe({
      next: (products) => {
        this.products.set(products);
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

  openDeleteDialog(product: ProductDto) {
    const dialogRef = this.dialog.open(ConfirmDeleteDialog, {
      data: { name: product.name },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.productsService.deleteProduct(product.id).subscribe({
          next: (result) => {
            this.snackBar.open(result.message, 'X', { duration: 3000 });
          },
          error: (err) => {
            this.snackBar.open(err.error.message, 'X', {
              panelClass: ['error-snackbar'],
            });
          },
          complete: () => {
            this.loadProducts();
          },
        });
      }
    });
  }

  openDetailDialog(product: ProductDto) {
    const dialogRef = this.dialog.open(ProductDetailDialog, {
      data: { product: product },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.router.navigate(['edit', product.id], { relativeTo: this.actRouter });
      }
    });
  }
}
