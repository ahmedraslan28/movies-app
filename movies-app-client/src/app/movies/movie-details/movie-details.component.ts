import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MoviesService } from '../../services/movies.service';
import { RateService } from '../../services/rate.service';
import { Movie } from '../../models/movie.model';
import { AuthService } from '../../services/auth.service';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-movie-details',
  templateUrl: './movie-details.component.html',
  styleUrls: ['./movie-details.component.css'],
})
export class MovieDetailsComponent implements OnInit {
  movie: Movie | null = null;
  loading = false;
  isAdmin = false;
  userRating = 0;
  selectedTab = 0;
  ratingInProgress = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private moviesService: MoviesService,
    private rateService: RateService,
    private authService: AuthService,
    private snackBar: MatSnackBar
  ) {
    this.isAdmin = this.authService.isAdmin();
  }

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      if (params['id']) {
        this.loadMovie(parseInt(params['id'], 10));
      }
    });
  }

  loadMovie(id: number): void {
    this.loading = true;
    this.moviesService.getMovieDetails(id).subscribe({
      next: (movie) => {
        this.movie = movie;
        this.loadUserRating(id);
      },
      error: (error) => {
        this.loading = false;
        this.snackBar.open('Error loading movie details', 'Close', {
          duration: 3000,
        });
        this.router.navigate(['/movies']);
      },
    });
  }

  loadUserRating(movieId: number): void {
    this.rateService.getUserRating(movieId).subscribe({
      next: (rating) => {
        this.userRating = rating;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      },
    });
  }

  onRateMovie(rating: number): void {
    if (!this.movie || this.ratingInProgress) return;

    const previousRating = this.userRating;
    this.userRating = rating;
    this.ratingInProgress = true;

    this.rateService
      .addRate(this.movie.id, rating)
      .pipe(finalize(() => (this.ratingInProgress = false)))
      .subscribe({
        next: (updatedMovie) => {
          this.movie = updatedMovie;
          this.snackBar.open('Rating updated successfully', 'Close', {
            duration: 2000,
          });
        },
        error: (error) => {
          this.userRating = previousRating; // Restore previous rating on error
          this.snackBar.open('Error updating rating', 'Close', {
            duration: 3000,
          });
        },
      });
  }

  onTabChange(index: number): void {
    this.selectedTab = index;
  }

  goBack(): void {
    this.router.navigate(['/movies']);
  }
}
