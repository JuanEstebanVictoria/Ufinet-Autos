import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Auto } from '../models/auto.model';

/** Filters accepted by the GET /api/autos endpoint. All fields are optional. */
export interface AutoFilters {
  plate?: string;
  brand?: string;
  year?: number | null;
}

/**
 * Service to manage automobile-related operations.
 */
@Injectable({
  providedIn: 'root'
})
export class AutoService {
  private apiUrl = 'http://localhost:8080/api/autos';

  constructor(private http: HttpClient) { }

  /**
   * Fetches the list of automobiles, optionally filtered by plate, brand, or year.
   * Any filter that is undefined, null, or an empty string is omitted from the request.
   *
   * @param filters optional search criteria
   * @returns an observable of the matching Auto objects
   */
  getAutos(filters: AutoFilters = {}): Observable<Auto[]> {
    let params = new HttpParams();
    if (filters.plate?.trim())        params = params.set('plate', filters.plate.trim());
    if (filters.brand?.trim())        params = params.set('brand', filters.brand.trim());
    if (filters.year != null)         params = params.set('year',  filters.year.toString());
    return this.http.get<Auto[]>(this.apiUrl, { params });
  }

  /**
   * Retrieves a single automobile by its ID.
   * @param id The unique identifier of the automobile.
   * @returns An observable of the Auto object.
   */
  getAuto(id: number): Observable<Auto> {
    return this.http.get<Auto>(`${this.apiUrl}/${id}`);
  }

  /**
   * Creates a new automobile entry.
   * @param auto The Auto data to be created.
   * @returns An observable containing the created Auto.
   */
  createAuto(auto: Auto): Observable<Auto> {
    return this.http.post<Auto>(this.apiUrl, auto);
  }

  /**
   * Updates an existing automobile entry.
   * @param id The unique identifier of the automobile to update.
   * @param auto The new data for the automobile.
   * @returns An observable of the updated Auto.
   */
  updateAuto(id: number, auto: Auto): Observable<Auto> {
    return this.http.put<Auto>(`${this.apiUrl}/${id}`, auto);
  }

  /**
   * Deletes an automobile entry by its ID.
   * @param id The unique identifier of the automobile to delete.
   * @returns An observable that completes when the deletion is done.
   */
  deleteAuto(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
