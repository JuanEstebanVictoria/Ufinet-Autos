import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { User } from '../models/user.model';

/**
 * Service for authentication operations.
 * SUGGESTION: Move the base API URL to an environment file to manage different environments (development, production).
 */
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';

  /** Reactive stream of the current login state. */
  private loggedIn$ = new BehaviorSubject<boolean>(!!localStorage.getItem('token'));

  /** Observable boolean that components and templates can subscribe to. */
  readonly isLoggedIn$ = this.loggedIn$.asObservable();

  constructor(private http: HttpClient) { }

  /**
   * Logs in a user by sending credentials to the server.
   * On success, stores the JWT token in local storage.
   * @param user The user credentials for login.
   * @returns An observable of the login response.
   */
  login(user: User): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, user).pipe(
      tap(res => {
        if (res.token) {
          localStorage.setItem('token', res.token);
          this.loggedIn$.next(true);
        }
      })
    );
  }

  /**
   * Registers a new user.
   * @param user The user data for registration.
   * @returns An observable of the registration response.
   */
  register(user: User): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/register`, user);
  }

  /**
   * Logs out the user by removing the JWT token from local storage.
   */
  logout(): void {
    localStorage.removeItem('token');
    this.loggedIn$.next(false);
  }

  /**
   * Checks if the user is currently logged in based on the presence of a JWT token.
   * @returns True if a token exists, false otherwise.
   */
  /** Synchronous check – prefer `isLoggedIn$` in templates. */
  isLoggedIn(): boolean {
    return this.loggedIn$.getValue();
  }

  /**
   * Retrieves the stored JWT token from local storage.
   * @returns The JWT token if found, null otherwise.
   */
  getToken(): string | null {
    return localStorage.getItem('token');
  }
}
