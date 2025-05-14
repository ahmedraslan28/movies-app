import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Movie } from '../models/movie.model';
import { OmdbMovie, OmdbSearchResponse } from '../models/omdb.model';

@Injectable({
  providedIn: 'root',
})
export class MoviesService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  searchMovies(
    title: string,
    page: number = 1
  ): Observable<OmdbSearchResponse> {
    return this.http.get<OmdbSearchResponse>(`${this.baseUrl}/movies/omdb`, {
      params: {
        searchParam: title,
        page: page.toString(),
      },
    });
  }

  addMoviesToLibrary(imdbIds: string[]): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/movies`, imdbIds);
  }

  getLibraryMovies(
    page: number = 1,
    size: number = 10
  ): Observable<{ movies: Movie[]; total: number }> {
    return this.http.get<{ movies: Movie[]; total: number }>(
      `${this.baseUrl}/movies`,
      {
        params: {
          page: page.toString(),
          size: size.toString(),
        },
      }
    );
  }

  removeFromLibrary(movieId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/movies/${movieId}`);
  }

  removeMultipleFromLibrary(movieIds: number[]): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/movies`, {
      body: movieIds,
    });
  }

  rateMovie(movieId: number, rating: number): Observable<Movie> {
    return this.http.post<Movie>(`${this.baseUrl}/movies/${movieId}/rate`, {
      rating,
    });
  }

  getMovieDetails(movieId: number): Observable<Movie> {
    return this.http.get<Movie>(`${this.baseUrl}/movies/${movieId}`);
  }
}
