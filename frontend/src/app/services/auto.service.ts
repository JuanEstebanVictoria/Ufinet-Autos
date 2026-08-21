import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Auto } from '../models/auto.model';

/**
 * Service to manage automobile-related operations.
 * SUGGESTION: Centralize the base API URL in an environment file for consistency and configurability.
 */
@Injectable({
  providedIn: 'root'
})
export class AutoService {
  private apiUrl = 'http://localhost:8080/api/autos';

  constructor(private http: HttpClient) { }

  /**
   * Fetches the list of all automobiles.
   * @returns An observable of an array of Auto objects.
   */
  getAutos(): Observable<Auto[]> {
    return this.http.get<Auto[]>(this.apiUrl);
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
