import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ProductDto } from '../models/dtos/product-dto';
import { SuccessResponse } from '../models/dtos/success-response';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private readonly API = '/api/products';
  http = inject(HttpClient);

  createProduct(productDto: ProductDto): Observable<SuccessResponse> {
    return this.http.post<SuccessResponse>(`${this.API}/create`, productDto);
  }

  updateProduct(productDto: ProductDto): Observable<SuccessResponse> {
    return this.http.put<SuccessResponse>(`${this.API}/update/${productDto.id}`, productDto);
  }

  deleteProduct(id: string): Observable<SuccessResponse> {
    return this.http.delete<SuccessResponse>(`${this.API}/delete/${id}`);
  }

  getAllProducts(): Observable<ProductDto[]> {
    return this.http.get<ProductDto[]>(`${this.API}/all`);
  }

  getProductById(id: string): Observable<ProductDto> {
    return this.http.get<ProductDto>(`${this.API}/get/${id}`);
  }
}
