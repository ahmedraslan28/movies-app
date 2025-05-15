import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Movie } from '../models/movie.model';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class RateService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient, private authService: AuthService) {}

  addRate(movieId: number, rating: number): Observable<Movie> {
    return this.http.post<Movie>(`${this.baseUrl}/movies/${movieId}/rate`, {
      rating,
    });
  }

  getUserRating(movieId: number): Observable<number> {
    return this.http.get<number>(
      `${this.baseUrl}/movies/${movieId}/user-rating`
    );
  }
}
