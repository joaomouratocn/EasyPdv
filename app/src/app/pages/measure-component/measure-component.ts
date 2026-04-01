import { Component, computed, inject, model, signal } from '@angular/core';
import { MeasureService } from '../../services/measure-service';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MeasureDto } from '../../models/dtos/measure-dto';
import { MeasureDialog } from '../../dialogs/measure-dialog/measure-dialog';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-measure-component',
  imports: [CommonModule, FormsModule, MatProgressSpinnerModule, MatSnackBarModule],
  templateUrl: './measure-component.html',
  styleUrl: './measure-component.css',
})
export class MeasureComponent {
  private measureService = inject(MeasureService);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);

  measureName = model<string>('');
  loading = model<boolean>(true);

  measure = signal<any[]>([]);
  filteredMeasure = computed(() => {
    return this.measure()
      .filter((measure) => measure.name.toLowerCase().includes(this.measureName().toLowerCase()))
      .sort((a, b) => a.name.localeCompare(b.name));
  });

  ngOnInit() {
    this.loadMeasure();
  }

  updateSearch(value: string) {
    this.measureName.set(value);
  }

  loadMeasure() {
    this.loading.set(true);
    this.measureService.getAllMeasures().subscribe({
      next: (measure) => {
        this.measure.set(measure);
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

  openDialogMeasure(measure: MeasureDto) {
    const dialogRef = this.dialog.open(MeasureDialog, {
      disableClose: true,
      data: { measure: measure.name },
    });

    dialogRef.afterClosed().subscribe((result: string) => {
      if (measure.id === '') {
        this.measureService.createMeasure(result).subscribe({
          next: (response) => {
            this.snackBar.open(response.message, 'OK', { duration: 2000 });
          },
          error: (err) => {
            this.snackBar.open(err.error.message, 'X', {
              panelClass: ['error-snackbar'],
            });
          },
          complete: () => {
            this.loadMeasure();
          },
        });
      }
    });
  }

  openDialogDeleteMeasure(measure: MeasureDto) {
    const dialogRef = this.dialog.open(MeasureDialog, {
      disableClose: true,
      data: { measure: measure.name },
    });

    dialogRef.afterClosed().subscribe((result: string) => {
      if (result) {
        this.measureService.deleteMeasure(measure.id).subscribe({
          next: (response) => {
            this.snackBar.open(response.message, 'OK', { duration: 2000 });
          },
          error: (err) => {
            this.snackBar.open(err.error.message, 'X', {
              panelClass: ['error-snackbar'],
            });
          },
          complete: () => {
            this.loadMeasure();
          },
        });
      }
    });
  }
}
