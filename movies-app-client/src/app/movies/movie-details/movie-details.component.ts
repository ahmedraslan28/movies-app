import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MoviesService } from '../../services/movies.service';
import { Movie } from '../../models/movie.model';
import { AuthService } from '../../services/auth.service';

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

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private moviesService: MoviesService,
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
        this.userRating = movie.rating || 0;
        this.loading = false;
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

  onRateMovie(rating: number): void {
    if (!this.movie) return;

    this.moviesService.rateMovie(this.movie.id, rating).subscribe({
      next: (updatedMovie) => {
        this.movie = updatedMovie;
        this.userRating = rating;
        this.snackBar.open('Rating updated successfully', 'Close', {
          duration: 2000,
        });
      },
      error: (error) => {
        this.snackBar.open('Error updating rating', 'Close', {
          duration: 3000,
        });
      },
    });
  }

  // removeMovie(): void {
  //   if (!this.movie || !this.isAdmin) return;

  //   if (confirm('Are you sure you want to remove this movie?')) {
  //     this.moviesService.removeFromLibrary(this.movie.id).subscribe({
  //       next: () => {
  //         this.snackBar.open('Movie removed successfully', 'Close', {
  //           duration: 2000,
  //         });
  //         this.router.navigate(['/movies']);
  //       },
  //       error: (error) => {
  //         this.snackBar.open('Error removing movie', 'Close', {
  //           duration: 3000,
  //         });
  //       },
  //     });
  //   }
  // }

  goBack(): void {
    this.router.navigate(['/movies']);
  }
}
