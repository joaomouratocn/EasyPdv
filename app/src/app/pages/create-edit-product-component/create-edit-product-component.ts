import { Component, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute } from '@angular/router';
import { ProductDto } from '../../models/dtos/product-dto';
import { ProductService } from '../../services/product-service';
import { CategoryDto } from '../../models/dtos/category-dto';
import { MeasureDto } from '../../models/dtos/measure-dto';

@Component({
  selector: 'app-create-edit-product-component',
  imports: [ReactiveFormsModule],
  templateUrl: './create-edit-product-component.html',
  styleUrl: './create-edit-product-component.css',
})
export class CreateEditProductComponent {
  private router = inject(ActivatedRoute);
  private snackBar = inject(MatSnackBar);
  private productService = inject(ProductService);

  loading = signal(false);
  actionType = signal('');

  receivedProduct = new FormGroup({
    id: new FormControl<string>(''),
    name: new FormControl<string>(''),
    description: new FormControl<string>(''),
    barcode: new FormControl<string>(''),
    category: new FormControl<CategoryDto | null>(null),
    measure: new FormControl<MeasureDto | null>(null),
    stock: new FormControl<number>(0),
    buy_price: new FormControl<number>(0),
    sale_price: new FormControl<number>(0),
    min_stock: new FormControl<number>(0),
    alert_stock: new FormControl<boolean>(true),
  });

  ngOnInit() {
    const id = this.router.snapshot.paramMap.get('id');

    if (id) {
      this.loading.set(true);
      this.actionType.set('Alterar produto');
      this.productService.getProductById(id).subscribe({
        next: (result) => {
          console.log(result);
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
    } else {
      this.actionType.set('Alterar produto');
    }
  }

  loadForm(product: ProductDto) {
    this.receivedProduct.patchValue({
      id: product.id,
      name: product.name,
      description: product.description,
      barcode: product.barcode,
      //category: product.category,
      //measure: product.measure,
      stock: product.stock,
      buy_price: product.buy_price,
      sale_price: product.sale_price,
      min_stock: product.min_stock,
      alert_stock: product.alert_stock,
    });
  }

  formatCurrent(event: any) {
    let value = event.target.value.replace(/\D/g, ''); // Remove tudo que não é dígito
    value = (Number(value) / 100).toLocaleString('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    });
    this.receivedProduct.get('buy_price')?.setValue(value, { emitEvent: false });
  }
}
