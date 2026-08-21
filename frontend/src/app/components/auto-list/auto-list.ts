import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AutoService, AutoFilters } from '../../services/auto.service';
import { Auto } from '../../models/auto.model';

/**
 * Displays the user's cars with a filter dropdown.
 * The user picks a filter type (plate, brand, year), types a value,
 * and the table reloads from the backend with the selected filter applied.
 */
@Component({
  selector: 'app-auto-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './auto-list.html',
  styleUrls: ['./auto-list.css']
})
export class AutoListComponent implements OnInit {
  autos: Auto[] = [];
  errorMessage: string | null = null;

  /** Which filter the user selected from the dropdown */
  selectedFilter: 'none' | 'plate' | 'brand' | 'year' = 'none';

  /** The value the user typed into the search input */
  filterValue: string = '';

  constructor(
    private autoService: AutoService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadAutos();
  }

  /** Fetches cars from the backend with the current filter applied. */
  loadAutos(): void {
    const filters: AutoFilters = {};

    if (this.selectedFilter !== 'none' && this.filterValue.trim()) {
      const val = this.filterValue.trim();
      if (this.selectedFilter === 'plate') filters.plate = val;
      if (this.selectedFilter === 'brand') filters.brand = val;
      if (this.selectedFilter === 'year')  filters.year = Number(val) || null;
    }

    this.autoService.getAutos(filters).subscribe({
      next: data => {
        this.autos = data;
        this.errorMessage = null;
        this.cdr.detectChanges();
      },
      error: err => {
        console.error('Error loading autos:', err);
        this.errorMessage = 'Could not load cars. Please try again.';
        this.cdr.detectChanges();
      }
    });
  }

  /** Called when the user picks a different filter type from the dropdown. */
  onFilterTypeChange(): void {
    this.filterValue = '';
    this.loadAutos();
  }

  /** Called on every keystroke in the search input. */
  onFilterValueChange(): void {
    this.loadAutos();
  }

  /** Resets the dropdown and reloads all cars. */
  clearFilter(): void {
    this.selectedFilter = 'none';
    this.filterValue = '';
    this.loadAutos();
  }

  deleteAuto(id: number): void {
    if (confirm('Are you sure you want to delete this auto?')) {
      this.autoService.deleteAuto(id).subscribe(() => this.loadAutos());
    }
  }
}
