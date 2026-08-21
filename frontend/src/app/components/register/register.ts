import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

/**
 * Component for user registration.
 */
@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './register.html'
})
export class RegisterComponent {
  /** The reactive form for registration data. */
  registerForm: FormGroup;
  /** Stores registration error messages. */
  error = '';
  /** Stores registration success messages. */
  successMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(4)]]
    });
  }

  /**
   * Submits registration data.
   * On success, clears form, shows success message, and redirects after delay.
   */
  onSubmit(): void {
    if (this.registerForm.valid) {
      this.error = '';
      this.successMessage = '';
      this.authService.register(this.registerForm.value).subscribe({
        next: () => {
          this.successMessage = 'Registration was successful. Redirecting to login...';
          this.registerForm.reset();
          this.cdr.detectChanges();
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 2000);
        },
        error: () => {
          this.error = 'Registration failed. Username might be taken.';
          this.cdr.detectChanges();
        }
      });
    }
  }
}
