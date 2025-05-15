import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { PageEvent } from '@angular/material/paginator';
import { MoviesService } from '../../services/movies.service';
import { Movie } from '../../models/movie.model';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-dashboard',
  templateUrl: './user-dashboard.component.html',
  styleUrls: ['./user-dashboard.component.css'],
})
export class UserDashboardComponent implements OnInit {
  movies: Movie[] = [];
  loading = false;
  totalMovies = 0;
  currentPage = 0;
  pageSize = 12;
  searchControl = new FormControl('');
  userName: string = '';

  constructor(
    private moviesService: MoviesService,
    private authService: AuthService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

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

  // onRateMovie(movie: Movie, rating: number): void {
  //   this.moviesService.rateMovie(movie.id, rating).subscribe({
  //     next: (updatedMovie) => {
  //       const index = this.movies.findIndex((m) => m.id === updatedMovie.id);
  //       if (index !== -1) {
  //         this.movies[index] = updatedMovie;
  //       }
  //       this.snackBar.open('Rating updated successfully', 'Close', {
  //         duration: 2000,
  //       });
  //     },
  //     error: (error) => {
  //       this.snackBar.open('Error updating rating', 'Close', {
  //         duration: 3000,
  //       });
  //     },
  //   });
  // }

  viewDetails(movieId: number): void {
    this.router.navigate(['/movies', movieId]);
  }

  logout(): void {
    this.authService.logout();
  }
}
