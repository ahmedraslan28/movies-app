import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Movie } from '../models/movie.model';
import { OmdbSearchResponse } from '../models/omdb.model';
import { MOVIES_URL } from '../constants';

@Injectable({
  providedIn: 'root',
})
export class MoviesService {
  constructor(private http: HttpClient) {}

  searchMovies(
    title: string,
    page: number = 1
  ): Observable<OmdbSearchResponse> {
    return this.http.get<OmdbSearchResponse>(`${MOVIES_URL}/omdb`, {
      params: {
        searchParam: title,
        page: page.toString(),
      },
    });
  }

  addMoviesToLibrary(imdbIds: string[]): Observable<any> {
    return this.http.post<any>(MOVIES_URL, imdbIds);
  }
  addSingleMovie(imdbID : string): Observable<any> {
    return this.http.post<any>(`${MOVIES_URL}/addMovie`, imdbID);
  }

  getLibraryMovies(
    page: number,
    size: number,
    title?: string
  ): Observable<{ movies: Movie[]; total: number }> {
    const params: { [key: string]: string } = {
      page: (page).toString(),
      size: size.toString(),
    };

    if (title) {
      params['title'] = title;
    }

    return this.http.get<{ movies: Movie[]; total: number }>(
      MOVIES_URL,
      {
        params,
      }
    );
  }
  deleteMovies(movieIds: number[]): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(
      `${MOVIES_URL}/deletePatch`,
      movieIds
    );
  }

  deleteMovie(movieId: number): Observable<{ message: string }> {
    return this.http.delete<{ message: string }>(
      `${MOVIES_URL}/${movieId}`,
      {}
    );
  }

  getMovieDetails(movieId: number): Observable<{ movie: Movie, userRate: number }> {
    return this.http.get<{ movie: Movie, userRate: number }>(`${MOVIES_URL}/${movieId}`);
  }
}
