import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Movie } from '../models/movie.model';
import { AuthService } from './auth.service';
import { User } from '../models/user.model';
import { MOVIES_URL } from '../constants';

@Injectable({
  providedIn: 'root',
})
export class RateService {
  constructor(private http: HttpClient, private authService: AuthService) {}

  currentUser: User | null = null;

  addRate(movieId: number, rating: number): Observable<{message : string, updatedMovie: Movie}> {
    return this.http.post<{message : string, updatedMovie: Movie}>(`${MOVIES_URL}/${movieId}/rate`, {
      rate : rating
    });
  }

  getUserRating(movieId: number): Observable<{message : string, value: number}> {
    return this.http.get<{message : string, value: number}>(
      `${MOVIES_URL}/${movieId}/rate`
    );
  }
}
