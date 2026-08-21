/**
 * ==========================================================
 * Authentication Guard for Autos Challenge
 * ==========================================================
 * Purpose: Protect routes from unauthorized access by checking the 
 * user's authentication status before allowing navigation.
 */

import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from './services/auth.service';

/**
 * Functional Route Guard: authGuard
 * This guard ensures that the user is logged in before accessing a route.
 */
export const authGuard: CanActivateFn = (route, state) => {
  // Purpose: Inject dependencies into the functional guard.
  const authService = inject(AuthService);
  const router = inject(Router);

  // Logic Section: Verify authentication status.
  if (authService.isLoggedIn()) {
    // If authenticated, allow navigation to the requested route.
    return true;
  } else {
    // If not authenticated, redirect the user to the login page.
    router.navigate(['/login']);
    return false;
  }
};
