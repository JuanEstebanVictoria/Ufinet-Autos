import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AutoService } from '../../services/auto.service';
import { Auto } from '../../models/auto.model';

/**
 * Component to display and manage a list of automobiles.
 * Uses ChangeDetectorRef.detectChanges() to explicitly trigger view updates
 * since the HTTP response may arrive outside Angular's change detection zone.
 */
@Component({
  selector: 'app-auto-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './auto-list.html',
  styleUrls: ['./auto-list.css']
})
export class AutoListComponent implements OnInit {
  autos: Auto[] = [];
  errorMessage: string | null = null;

  constructor(
    private autoService: AutoService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadAutos();
  }

  loadAutos(): void {
    this.autoService.getAutos().subscribe({
      next: data => {
        this.autos = data;
        this.cdr.detectChanges(); // Force Angular to update the view
      },
      error: err => {
        console.error('Error loading autos:', err);
        this.errorMessage = 'Could not load cars. Please try again.';
        this.cdr.detectChanges();
      }
    });
  }

  deleteAuto(id: number): void {
    if (confirm('Are you sure you want to delete this auto?')) {
      this.autoService.deleteAuto(id).subscribe(() => this.loadAutos());
    }
  }
}
