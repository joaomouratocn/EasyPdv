import { Component, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ProductDto } from '../../models/dtos/product-dto';
import { ProductService } from '../../services/product-service';
import { CategoryDto } from '../../models/dtos/category-dto';
import { MeasureDto } from '../../models/dtos/measure-dto';
import { CategoryService } from '../../services/category-service';
import { MeasureService } from '../../services/measure-service';

@Component({
  selector: 'app-create-edit-product-component',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './create-edit-product-component.html',
  styleUrl: './create-edit-product-component.css',
})
export class CreateEditProductComponent {
  private router = inject(ActivatedRoute);
  private snackBar = inject(MatSnackBar);
  private productService = inject(ProductService);
  private categoryService = inject(CategoryService);
  private measureService = inject(MeasureService);

  categories = signal<CategoryDto[]>([]);
  measures = signal<MeasureDto[]>([]);
  loading = signal(false);
  actionType = signal('');
  actionButtonText = signal('');

  receivedProduct = new FormGroup({
    id: new FormControl<string>(''),
    name: new FormControl<string>('', [Validators.required, Validators.minLength(4)]),
    description: new FormControl<string>(''),
    barcode: new FormControl<string>('', [Validators.required, Validators.pattern('^[0-9]*$')]),
    category: new FormControl<CategoryDto | null>(null, [Validators.required]),
    measure: new FormControl<MeasureDto | null>(null, [Validators.required]),
    min_stock: new FormControl<number>(1, [Validators.required, Validators.min(1)]),
    alert_stock: new FormControl<boolean>(true),
  });

  ngOnInit() {
    this.loadCategoriesAndMeasure();
    this.valideteReceivedProduct();
  }

  compareObj(o1: any, o2: any): boolean {
    if (o1 === o2) return true;
    if (!o1 || !o2) return false;
    return o1.id === o2.id;
  }

  saveProduct() {
    if (this.receivedProduct.get('id') && this.receivedProduct.valid) {
      this.productService.updateProduct(this.receivedProduct);
    }
  }

  loadForm(product: ProductDto) {
    this.receivedProduct.patchValue({
      id: product.id,
      name: product.name,
      description: product.description,
      barcode: product.barcode,
      category: product.category,
      measure: product.measure,
      min_stock: product.min_stock,
      alert_stock: product.alert_stock,
    });
  }

  /*
  formatCurrent(event: any) {
    let value = event.target.value.replace(/\D/g, ''); // Remove tudo que não é dígito
    value = (Number(value) / 100).toLocaleString('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    });
    this.receivedProduct.get('buy_price')?.setValue(value, { emitEvent: false });
  }
  */

  valideteReceivedProduct() {
    const id = this.router.snapshot.paramMap.get('id');

    if (id) {
      this.loading.set(true);
      this.actionType.set('Alterar produto');
      this.actionButtonText.set('Salvar');
      this.productService.getProductById(id).subscribe({
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
    } else {
      this.actionType.set('Novo produto');
      this.actionButtonText.set('Cadastrar');
    }
  }

  loadCategoriesAndMeasure() {
    this.categoryService.getAllCategories().subscribe({
      next: (result) => {
        this.categories.set(result);
      },
      error: (err) => {
        this.snackBar.open(err.error.message, 'X', {
          panelClass: ['error-snackbar'],
        });
      },
    });

    this.measureService.getAllMeasures().subscribe({
      next: (result) => {
        this.measures.set(result);
      },
      error: (err) => {
        this.snackBar.open(err.error.message, 'X', { panelClass: ['error-snackbar'] });
      },
    });
  }
}
