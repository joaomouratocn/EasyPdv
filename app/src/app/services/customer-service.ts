import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { CustomerDto } from '../models/dtos/customer-dto';
import { Observable } from 'rxjs';
import { SuccessResponse } from '../models/dtos/success-response';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  private readonly API = '/api/customers';
  private http = inject(HttpClient);

  createCustomer(customerDto: CustomerDto): Observable<SuccessResponse> {
    return this.http.post<SuccessResponse>(`${this.API}/create`, customerDto);
  }

  updateCustomer(customerDto: CustomerDto): Observable<SuccessResponse> {
    return this.http.put<SuccessResponse>(`${this.API}/create`, customerDto);
  }

  deleteCustomer(id: string): Observable<SuccessResponse> {
    return this.http.delete<SuccessResponse>(`${this.API}/delete/${id}`);
  }

  getAllCustomers(): Observable<CustomerDto[]> {
    return this.http.get<CustomerDto[]>(`${this.API}/all`);
  }
}
