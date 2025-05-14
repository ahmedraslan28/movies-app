import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { PageEvent } from '@angular/material/paginator';
import { MoviesService } from '../../services/movies.service';
import { OmdbMovie } from '../../models/omdb.model';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css'],
})
export class AdminDashboardComponent implements OnInit {
  searchControl = new FormControl('');
  loading = false;
  movies: OmdbMovie[] = [];
  selectedMovies = new Set<string>();
  totalResults = 0;
  currentPage = 1;
  pageSize = 10;

  constructor(
    private moviesService: MoviesService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {}

  onSearch(): void {
    if (!this.searchControl.value) {
      return;
    }

    this.loading = true;
    this.moviesService
      .searchMovies(this.searchControl.value, this.currentPage)
      .subscribe({
        next: (response) => {
          this.loading = false;
          if (response.Response === 'True') {
            this.movies = response.Search;
            this.totalResults = parseInt(response.totalResults, 10);
          } else {
            this.movies = [];
            this.totalResults = 0;
            this.snackBar.open('No movies found', 'Close', { duration: 3000 });
          }
        },
        error: (error) => {
          this.loading = false;
          this.snackBar.open('Error searching movies', 'Close', {
            duration: 3000,
          });
        },
      });
  }

  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex + 1;
    this.pageSize = event.pageSize;
    console.log(this.pageSize);
    this.onSearch();
  }

  toggleSelection(movie: OmdbMovie, event: any = null): void {
    // If event exists, use its checked state, otherwise toggle
    const selected = event ? event.checked : !this.selectedMovies.has(movie.imdbID);
    
    if (selected) {
      this.selectedMovies.add(movie.imdbID);
    } else {
      this.selectedMovies.delete(movie.imdbID);
    }
  }

  isSelected(movie: OmdbMovie): boolean {
    return this.selectedMovies.has(movie.imdbID);
  }

  selectAll(): void {
    // Clear if all are selected, otherwise select all
    const allSelected = this.movies.every(movie => this.selectedMovies.has(movie.imdbID));
    
    if (allSelected) {
      this.selectedMovies.clear();
    } else {
      this.movies.forEach(movie => this.selectedMovies.add(movie.imdbID));
    }
  }

  addSelectedMovies(): void {
    const selectedMovies = this.movies.filter((movie) =>
      this.selectedMovies.has(movie.imdbID)
    );
    let addedCount = 0;
    let errorCount = 0;

    selectedMovies.forEach((movie) => {
      this.moviesService.addMovieToLibrary(movie).subscribe({
        next: () => {
          addedCount++;
          if (addedCount + errorCount === selectedMovies.length) {
            this.showResultMessage(addedCount, errorCount);
          }
        },
        error: () => {
          errorCount++;
          if (addedCount + errorCount === selectedMovies.length) {
            this.showResultMessage(addedCount, errorCount);
          }
        },
      });
    });
  }

  private showResultMessage(addedCount: number, errorCount: number): void {
    const message =
      errorCount === 0
        ? `Successfully added ${addedCount} movies`
        : `Added ${addedCount} movies, failed to add ${errorCount} movies`;
    this.snackBar.open(message, 'Close', { duration: 3000 });
    this.selectedMovies.clear();
  }
}
