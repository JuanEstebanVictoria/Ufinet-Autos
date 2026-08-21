import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, Router, RouterModule } from '@angular/router';
import { AuthService } from './services/auth.service';

/**
 * Root component of the application.
 */
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterModule],
  templateUrl: './app.html'
})
export class AppComponent {
  constructor(public authService: AuthService, private router: Router) {}

  /**
   * Logs out the current user and redirects to the login page.
   */
  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
