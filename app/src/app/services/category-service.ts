import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CategoryDto } from '../models/dtos/category-dto';
import { SuccessResponse } from '../models/dtos/success-response';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  private readonly API = '/api/categories';

  constructor(private http: HttpClient) {}

  createCategory(name: string): Observable<SuccessResponse> {
    const params = new HttpParams().set('name', name);
    return this.http.post<SuccessResponse>(`${this.API}/create`, null, { params });
  }

  updateCategory(name: string, id: number): Observable<SuccessResponse> {
    const params = new HttpParams().set('name', name).set('id', id.toString());
    return this.http.put<SuccessResponse>(`${this.API}/update`, null, { params });
  }

  deleteCategory(id: number): Observable<SuccessResponse> {
    const params = new HttpParams().set('id', id.toString());
    return this.http.delete<SuccessResponse>(`${this.API}/delete`, { params });
  }

  getAllCategories(): Observable<CategoryDto[]> {
    return this.http.get<CategoryDto[]>(`${this.API}/all`);
  }
}
