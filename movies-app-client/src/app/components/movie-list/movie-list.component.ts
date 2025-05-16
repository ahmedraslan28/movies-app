import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { PageEvent } from '@angular/material/paginator';
import { MoviesService } from '../../services/movies.service';
import { Movie } from '../../models/movie.model';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-movie-list',
  templateUrl: './movie-list.component.html',
  styleUrls: ['./movie-list.component.css'],
})
export class MovieListComponent implements OnInit {
  movies: Movie[] = [];
  loading = false;
  totalMovies = 0;
  currentPage = 0;
  pageSize = 9;
  isAdmin = false;
  selectedMovies = new Set<number>();
  searchQuery = '';
  isDeleting = false;
  private searchTimeout: any;

  constructor(
    private moviesService: MoviesService,
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar,
    private route: ActivatedRoute
  ) {
    this.isAdmin = this.authService.isAdmin();
  }

  ngOnInit(): void {
    const params = this.route.snapshot.queryParamMap;

    const page = parseInt(params.get('page') || '0', 10);
    const size = parseInt(params.get('size') || '9', 10);

    this.currentPage = page >= 0 ? page : 0;
    this.pageSize = size > 0 ? size : 9;

    this.loadMovies();
  }
  onSearch(query: string): void {
    this.searchQuery = query;
    clearTimeout(this.searchTimeout);
    this.searchTimeout = setTimeout(() => {
      this.currentPage = 0;
      this.addParamsToUrl();
      this.loadMovies();
    }, 300);
  }

  loadMovies(): void {
    this.loading = true;
    this.selectedMovies.clear(); // Clear selection when reloading movies
    this.moviesService
      .getLibraryMovies(this.currentPage, this.pageSize, this.searchQuery)
      .subscribe({
        next: (response) => {
          this.movies = response.movies;
          this.totalMovies = response.total;
          this.loading = false;
        },
        error: (error) => {
          this.loading = false;
          this.snackBar.open(
            error.error.message || 'Error loading movies',
            'Close',
            {
              duration: 3000,
            }
          );
        },
      });
  }

  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.addParamsToUrl();
    this.loadMovies();
  }

  viewDetails(movieId: number): void {
    this.router.navigate(['/movies', movieId]);
  }

  toggleMovieSelection(movieId: number, event: Event): void {
    if (this.selectedMovies.has(movieId)) {
      this.selectedMovies.delete(movieId);
    } else {
      this.selectedMovies.add(movieId);
    }
  }

  isMovieSelected(movieId: number): boolean {
    return this.selectedMovies.has(movieId);
  }

  deleteSelectedMovies(): void {
    if (this.selectedMovies.size === 0) return;

    if (confirm('Are you sure you want to remove selected movies?')) {
      const movieIds = Array.from(this.selectedMovies);
      this.isDeleting = true;

      this.moviesService.deleteMovies(movieIds).subscribe({
        next: () => {
          this.isDeleting = false;
          this.selectedMovies.clear();
          this.loadMovies();
          this.snackBar.open(
            `Successfully deleted ${movieIds.length} movies`,
            'Close',
            {
              duration: 3000,
            }
          );
        },
        error: (error) => {
          console.log(error);
          this.isDeleting = false;
          this.snackBar.open('Error deleting movies', 'Close', {
            duration: 3000,
          });
        },
      });
    }
  }

  deleteMovie(movieId: number, event: Event): void {
    event.stopPropagation();
    if (!this.isAdmin) return;

    if (confirm('Are you sure you want to remove this movie?')) {
      this.moviesService.deleteMovie(movieId).subscribe({
        next: (response) => {
          this.snackBar.open(response.message, 'Close', {
            duration: 2000,
          });
          this.loadMovies();
        },
        error: (error) => {
          this.snackBar.open('Error removing movie', 'Close', {
            duration: 3000,
          });
        },
      });
    }
  }

  goToAdminDashboard(): void {
    this.router.navigate(['/admin/dashboard']);
  }

  logout(): void {
    this.authService.logout();
  }

  addParamsToUrl(): void {
    this.router.navigate([], {
      queryParams: { page: this.currentPage },
      queryParamsHandling: 'merge',
    });
  }
}
