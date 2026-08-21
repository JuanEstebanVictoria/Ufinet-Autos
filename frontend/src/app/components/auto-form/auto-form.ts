import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { AutoService } from '../../services/auto.service';
import { Auto } from '../../models/auto.model';

/**
 * Component for creating and editing automobile entries.
 */
@Component({
  selector: 'app-auto-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './auto-form.html'
})
export class AutoFormComponent implements OnInit {
  /** The reactive form for auto data. */
  autoForm: FormGroup;
  /** Indicates if the form is in edit mode. */
  isEditMode = false;
  /** The ID of the automobile being edited, if applicable. */
  autoId?: number;
  /** Error message to display if the server request fails. */
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private autoService: AutoService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.autoForm = this.fb.group({
      brand: ['', Validators.required],
      model: ['', Validators.required],
      year: [new Date().getFullYear(), [Validators.required, Validators.min(1900)]],
      licensePlate: ['', [Validators.required, Validators.pattern('^[A-Z0-9-]+$')]],
      color: ['', Validators.required]
    });
  }

  /**
   * Initializes the component. Determines if it's in edit mode and loads the data if so.
   */
  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.autoId = +id;
      this.autoService.getAuto(this.autoId).subscribe({
        next: (auto) => this.autoForm.patchValue(auto),
        error: (err) => this.errorMessage = "Failed to load auto data."
      });
    }
  }

  /**
   * Submits the form data to create or update an automobile.
   * Handles server-side errors and displays them to the user.
   */
  onSubmit(): void {
    if (this.autoForm.valid) {
      this.errorMessage = null; // Clear previous errors
      const autoData: Auto = this.autoForm.value;
      
      const handleResponse = {
        next: () => {
          this.router.navigate(['/']);
        },
        error: (err: any) => {
          // Attempt to extract the error message from the backend JSON response.
          this.errorMessage = err.error?.message || err.error || "An unexpected error occurred.";
          console.error("Submission error:", err);
        }
      };

      if (this.isEditMode && this.autoId) {
        this.autoService.updateAuto(this.autoId, autoData).subscribe(handleResponse);
      } else {
        this.autoService.createAuto(autoData).subscribe(handleResponse);
      }
    }
  }
}
