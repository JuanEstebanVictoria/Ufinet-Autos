import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

/**
 * Component for user login.
 */
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.html'
})
export class LoginComponent {
  /** The reactive form for login credentials. */
  loginForm: FormGroup;
  /** Stores login error messages. */
  error = '';
  /** Stores login success messages. */
  successMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  /**
   * Submits user credentials for login.
   * On success, clears form, shows success message, and redirects after delay.
   */
  onSubmit(): void {
    if (this.loginForm.valid) {
      this.error = '';
      this.successMessage = '';
      this.authService.login(this.loginForm.value).subscribe({
        next: () => {
          this.successMessage = '¡Bienvenido! Iniciando sesión...';
          this.loginForm.reset();
          setTimeout(() => {
            this.router.navigate(['/']);
          }, 1000);
        },
        error: () => {
          this.error = 'Username or password is incorrect';
        }
      });
    }
  }
}
