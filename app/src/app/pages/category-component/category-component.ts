import { Component, computed, inject, model, signal } from '@angular/core';
import { CategoryService } from '../../services/category-service';
import { CategoryDto } from '../../models/dtos/category-dto';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CategooryDialog } from '../../dialogs/categoory-dialog/categoory-dialog';
import { MatDialog } from '@angular/material/dialog';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ConfirmDeleteDialog } from '../../dialogs/confirm-delete-dialog/confirm-delete-dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-category-component',
  imports: [CommonModule, FormsModule, MatProgressSpinnerModule, MatSnackBarModule],
  templateUrl: './category-component.html',
  styleUrl: './category-component.css',
})
export class CategoryComponent {
  private categoryService = inject(CategoryService);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);

  categoryName = model<string>('');
  loading = signal<boolean>(true);

  categories = signal<CategoryDto[]>([]);
  filteredCategories = computed(() => {
    return this.categories()
      .filter((category) => category.name.toLowerCase().includes(this.categoryName().toLowerCase()))
      .sort((a, b) => a.name.localeCompare(b.name));
  });

  ngOnInit() {
    this.loadCategories();
  }

  updateSearch(value: string) {
    this.categoryName.set(value);
  }

  loadCategories() {
    this.loading.set(true);
    this.categoryService.getAllCategories().subscribe({
      next: (categories) => {
        this.categories.set(categories);
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

  openDialogCategory(category: CategoryDto) {
    const dialogRef = this.dialog.open(CategooryDialog, {
      disableClose: true,
      width: '400px',
      data: { categoryName: category.name },
    });

    dialogRef.afterClosed().subscribe((result: string) => {
      if (result) {
        if (category.id === '') {
          this.categoryService.createCategory(result).subscribe({
            next: (response) => {
              this.snackBar.open(response.message, 'OK', { duration: 2000 });
            },
            error: (err) => {
              this.snackBar.open(err.error.message, 'X', {
                panelClass: ['error-snackbar'],
              });
            },
            complete: () => {
              this.loadCategories();
            },
          });
        } else {
          this.categoryService.updateCategory(result, category.id).subscribe({
            next: (response) => {
              this.snackBar.open(response.message, 'OK', { duration: 2000 });
            },
            error: (err) => {
              this.snackBar.open(err.error.message, 'X', {
                panelClass: ['error-snackbar'],
              });
            },
            complete: () => {
              this.loadCategories();
            },
          });
        }
      }
    });
  }

  openDialogDeleteCategory(category: CategoryDto) {
    const dialogRef = this.dialog.open(ConfirmDeleteDialog, {
      disableClose: true,
      width: '400px',
      data: { categoryName: category.name },
    });

    dialogRef.afterClosed().subscribe((result: boolean) => {
      if (result) {
        this.categoryService.deleteCategory(category.id).subscribe({
          next: (response) => {
            this.snackBar.open(response.message, 'OK', { duration: 2000 });
          },
          error: (err) => {
            this.snackBar.open(err.error.message, 'X', {
              panelClass: ['error-snackbar'],
            });
          },
          complete: () => {
            this.loadCategories();
          },
        });
      }
    });
  }
}
