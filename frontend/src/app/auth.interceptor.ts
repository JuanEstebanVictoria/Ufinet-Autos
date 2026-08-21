import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

/**
 * HTTP Interceptor: injects the JWT token and handles auth errors.
 * On 401/403, clears the stale token and redirects to /login automatically.
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token');
  const router = inject(Router);

  const authReq = token
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      // Don't intercept auth endpoints — let login/register handle their own errors
      const isAuthRequest = req.url.includes('/api/auth/');
      if (!isAuthRequest && (error.status === 401 || error.status === 403)) {
        // Token is invalid, expired, or missing — force re-login
        localStorage.removeItem('token');
        router.navigate(['/login']);
      }
      return throwError(() => error);
    })
  );
};
