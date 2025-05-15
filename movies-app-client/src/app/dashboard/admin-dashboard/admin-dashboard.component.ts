import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { PageEvent } from '@angular/material/paginator';
import { Router } from '@angular/router';
import { MoviesService } from '../../services/movies.service';
import { AuthService } from '../../services/auth.service';
import { OmdbMovie } from '../../models/omdb.model';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css'],
})
export class AdminDashboardComponent implements OnInit {
  searchControl = new FormControl('');
  loading = false;
  isAddingMovies = false;
  movies: OmdbMovie[] = [];
  selectedMovies = new Set<string>();

  totalResults = 0;
  currentPage = 1;
  pageSize = 10;

  constructor(
    private moviesService: MoviesService,
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {}

  // ========== Search ==========
  onSearch(): void {
    const query = this.searchControl.value?.trim();
    if (!query) return;

    this.loading = true;
    this.moviesService.searchMovies(query, this.currentPage).subscribe({
      next: ({ Response, Search, totalResults }) => {
        this.loading = false;

        if (Response === 'True') {
          this.movies = Search || [];
          this.totalResults = parseInt(totalResults, 10);
        } else {
          this.resetSearch('No movies found');
        }
      },
      error: () => this.resetSearch('Error from OMDb server. Try again.'),
    });
  }

  private resetSearch(message: string): void {
    this.loading = false;
    this.movies = [];
    this.totalResults = 0;
    this.snackBar.open(message, 'Close', { duration: 3000 });
  }

  // ========== Pagination ==========
  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex + 1;
    this.pageSize = event.pageSize;
    this.onSearch();
  }

  // ========== Movie Selection ==========
  toggleSelection(movie: OmdbMovie, checked?: boolean): void {
    const isSelected =
      checked !== undefined ? checked : !this.isSelected(movie);

    isSelected
      ? this.selectedMovies.add(movie.imdbID)
      : this.selectedMovies.delete(movie.imdbID);
  }

  isSelected(movie: OmdbMovie): boolean {
    return this.selectedMovies.has(movie.imdbID);
  }

  get areAllCurrentPageMoviesSelected(): boolean {
    return this.movies.every((movie) => this.selectedMovies.has(movie.imdbID));
  }

  selectAll(): void {
    // check if all movies in the current page are selected
    this.areAllCurrentPageMoviesSelected
      ? this.movies.forEach((m) => this.selectedMovies.delete(m.imdbID))
      : this.movies.forEach((m) => this.selectedMovies.add(m.imdbID));
  }
  // ========== Add Movies ==========
  addSelectedMovies(): void {
    const movieIds = Array.from(this.selectedMovies);
    if (movieIds.length === 0) return;

    this.isAddingMovies = true;
    this.moviesService.addMoviesToLibrary(movieIds).subscribe({
      next: () => {
        this.isAddingMovies = false;
        this.showResultMessage(movieIds.length, 0);
      },
      error: () => {
        this.isAddingMovies = false;
        this.showResultMessage(0, movieIds.length);
      }
    });
  }

  addSingleMovie(imdbID: string): void {
    this.isAddingMovies = true;
    this.moviesService.addSingleMovie(imdbID).subscribe({
      next: () => {
        this.isAddingMovies = false;
        this.snackBar.open('Movie added successfully', 'Close', { duration: 3000 });
      },
      error: (error) => {
        this.isAddingMovies = false;
        this.snackBar.open(error.error.message, 'Close', { duration: 3000 });
      }
    });
  }

  private showResultMessage(addedCount: number, errorCount: number): void {
    const message =
      errorCount === 0
        ? `Successfully added ${addedCount} movies`
        : `Failed to add ${errorCount} movies`;

    this.snackBar.open(message, 'Close', { duration: 3000 });
    this.selectedMovies.clear();
  }

  // ========== Logout ==========
  logout(): void {
    this.authService.logout();
  }
}
