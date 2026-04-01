import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SuccessResponse } from '../models/dtos/success-response';
import { MeasureDto } from '../models/dtos/measure-dto';

@Injectable({
  providedIn: 'root',
})
export class MeasureService {
  private readonly API = '/api/measures';
  private http = inject(HttpClient);

  createMeasure(name: string): Observable<SuccessResponse> {
    const params = new HttpParams().set('name', name);
    return this.http.post<SuccessResponse>(`${this.API}/create`, null, { params });
  }

  updateMeasure(name: string, id: string): Observable<SuccessResponse> {
    const params = new HttpParams().set('id', id).set('name', name);
    return this.http.put<SuccessResponse>(`${this.API}/update`, null, { params });
  }

  deleteMeasure(id: string): Observable<SuccessResponse> {
    const params = new HttpParams().set('id', id);
    return this.http.delete<SuccessResponse>(`${this.API}/delete`, { params });
  }

  getAllMeasures(): Observable<MeasureDto[]> {
    return this.http.get<MeasureDto[]>(`${this.API}/all`);
  }
}
