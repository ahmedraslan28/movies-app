import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
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
  pageSize = 12;
  isAdmin = false;

  constructor(
    private moviesService: MoviesService,
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.isAdmin = this.authService.isAdmin();
  }

  ngOnInit(): void {
    this.loadMovies();
  }

  loadMovies(): void {
    this.loading = true;
    this.moviesService
      .getLibraryMovies(this.currentPage + 1, this.pageSize)
      .subscribe({
        next: (response) => {
          this.movies = response.movies;
          this.totalMovies = response.total;
          this.loading = false;
        },
        error: (error) => {
          this.loading = false;
          this.snackBar.open('Error loading movies', 'Close', {
            duration: 3000,
          });
        },
      });
  }

  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadMovies();
  }

  viewDetails(movieId: number): void {
    this.router.navigate(['/movies', movieId]);
  }

  removeMovie(movieId: number, event: Event): void {
    event.stopPropagation();
    if (!this.isAdmin) return;

    if (confirm('Are you sure you want to remove this movie?')) {
      this.moviesService.removeFromLibrary(movieId).subscribe({
        next: () => {
          this.snackBar.open('Movie removed successfully', 'Close', {
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
}
