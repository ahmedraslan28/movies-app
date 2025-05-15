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
  userRating = 0;
  ratingInProgress = false;
  isAdmin = false;

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
    const id = +this.route.snapshot.params['id']; // get param and convert to number
    if (id) {
      this.loadMovie(id);
    } else {
      this.router.navigate(['/movies']); // fallback if id is invalid
    }
  }

  loadMovie(id: number): void {
    this.loading = true;
    this.moviesService.getMovieDetails(id).subscribe({
      next: (movie) => {
        this.movie = movie;
        this.loadUserRating(id);
      },
      error: (error) => {
        this.snackBar.open(error.message || 'Failed to load movie', 'Close', {
          duration: 3000,
        });
        this.router.navigate(['/movies']);
        this.loading = false;
      },
    });
  }

  loadUserRating(movieId: number): void {
    this.rateService.getUserRating(movieId).subscribe({
      next: (response) => {
        this.userRating = response.value;
        this.loading = false;
      },
      error: () => {
        this.userRating = 0;
        this.loading = false;
      },
    });
  }

  onRateMovie(rating: number): void {
    if (!this.movie || this.ratingInProgress) return;

    const previousRating = this.userRating;
    this.userRating = rating;
    this.ratingInProgress = true;

    this.rateService.addRate(this.movie.id, rating).subscribe({
      next: (response) => {
        this.ratingInProgress = false; // manually unset loading here
        this.movie = response.updatedMovie;
        this.snackBar.open(response.message, 'Close', { duration: 2000 });
      },
      error: (error) => {
        this.ratingInProgress = false; // manually unset loading here as well
        this.userRating = previousRating;
        this.snackBar.open(error.message || 'Rating failed', 'Close', {
          duration: 3000,
        });
      },
    });
  }

  goBack(): void {
    this.router.navigate(['/movies']);
  }
}
