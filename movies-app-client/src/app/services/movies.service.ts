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
    size: number = 10,
    title?: string
  ): Observable<{ movies: Movie[]; total: number }> {
    const params: { [key: string]: string } = {
      page: page.toString(),
      size: size.toString(),
    };

    if (title) {
      params['title'] = title;
    }

    return this.http.get<{ movies: Movie[]; total: number }>(
      `${this.baseUrl}/movies`,
      {
        params,
      }
    );
  }
  deleteMovies(movieIds: number[]): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(
      `${this.baseUrl}/movies/deletePatch`,
      movieIds
    );
  }

  deleteMovie(movieId: number): Observable<{ message: string }> {
    return this.http.delete<{ message: string }>(
      `${this.baseUrl}/movies/${movieId}`,
      {}
    );
  }

  getMovieDetails(movieId: number): Observable<Movie> {
    return this.http.get<Movie>(`${this.baseUrl}/movies/${movieId}`);
  }
}
