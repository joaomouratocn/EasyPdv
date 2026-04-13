import { Component, computed, inject, model, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CustomerDto } from '../../models/dtos/customer-dto';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { CustomerService } from '../../services/customer-service';
import { ConfirmDeleteDialog } from '../../dialogs/confirm-delete-dialog/confirm-delete-dialog';
import { CustomerDetailDialog } from '../../dialogs/customer-detail-dialog/customer-detail-dialog';

@Component({
  selector: 'app-customers',
  imports: [FormsModule, MatProgressSpinnerModule, RouterLink],
  templateUrl: './customers.html',
  styleUrl: './customers.css',
})
export class Customers {
  private customerService = inject(CustomerService);
  private actRouter = inject(ActivatedRoute);
  private snackBar = inject(MatSnackBar);
  private dialog = inject(MatDialog);
  private router = inject(Router);

  customerName = model('');
  loading = signal(false);
  customers = signal<CustomerDto[]>([]);
  filteredCustomer = computed(() => {
    return this.customers()
      .filter((customer) => customer.name.toLowerCase().includes(this.customerName().toLowerCase()))
      .sort((a, b) => a.name.localeCompare(b.name));
  });

  ngOnInit() {
    this.loadForm();
  }

  loadForm() {
    this.loading.set(true);
    this.customerService.getAllCustomers().subscribe({
      next: (result) => {
        this.customers.set(result);
      },
      error: (err) => {
        this.snackBar.open(err.message, 'X', { panelClass: ['error-snackbar'] });
      },
      complete: () => {
        this.loading.set(false);
      },
    });
  }

  openDetailDialog(customerDto: CustomerDto) {
    const dialogRef = this.dialog.open(CustomerDetailDialog, { data: { customer: customerDto } });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.router.navigate(['edit', customerDto.id], { relativeTo: this.actRouter });
      }
    });
  }

  openDeleteDialog(customerDto: CustomerDto) {
    const dialogRef = this.dialog.open(ConfirmDeleteDialog, { data: { name: customerDto.name } });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.customerService.deleteCustomer(customerDto.id).subscribe({
          next: (result) => {
            this.snackBar.open(result.message, 'X', { duration: 3000 });
            this.loadForm();
          },
          error: (err) => {
            this.snackBar.open(err.message, 'X', { panelClass: ['error-snackbar'] });
          },
        });
      }
    });
  }
}
