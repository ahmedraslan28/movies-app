import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MoviesService } from '../../services/movies.service';
import { RateService } from '../../services/rate.service';
import { Movie } from '../../models/movie.model';
import { AuthService } from '../../services/auth.service';
import { Location } from '@angular/common';

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
    private snackBar: MatSnackBar,
    private location: Location
  ) {
    this.isAdmin = this.authService.isAdmin();
  }

  ngOnInit(): void {
    const id = +this.route.snapshot.params['id'];
    if (id) {
      this.loadMovie(id);
    } else {
      this.router.navigate(['/movies']);
    }
  }

  loadMovie(id: number): void {
    this.loading = true;
    this.moviesService.getMovieDetails(id).subscribe({
      next: (response) => {
        this.movie = response.movie;
        this.userRating = response.userRate;
        this.loading = false;
      },
      error: (error) => {
        this.userRating = 0;
        this.loading = false;
        this.snackBar.open(error.message || 'Failed to load movie', 'Close', {
          duration: 3000,
        });
        this.router.navigate(['/movies']);
        this.loading = false;
      },
    });
  }

  onRateMovie(rating: number): void {
    if (!this.movie || this.ratingInProgress) return;
    this.ratingInProgress = true;
    this.rateService.addRate(this.movie.id, rating).subscribe({
      next: (response) => {
        this.ratingInProgress = false;
        this.userRating = rating;
        this.movie = response.updatedMovie;
        this.snackBar.open(response.message, 'Close', { duration: 2000 });
      },
      error: (error) => {
        console.log(error)
        this.ratingInProgress = false;
        this.snackBar.open(error.error.message || 'Rating failed', 'Close', {
          duration: 3000,
        });
      },
    });
  }

  goBack(): void {
    this.location.back();
  }
}
