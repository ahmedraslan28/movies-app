package com.raslan.mapper;

import com.raslan.dto.movie.MovieRequest;
import com.raslan.dto.movie.MovieResponse;
import com.raslan.model.Movie;

public class MovieMapper {
    public static Movie toMovie(MovieRequest movieRequest) {
        return Movie.builder()
                .title(movieRequest.getTitle())
                .description(movieRequest.getDescription())
                .poster(movieRequest.getPoster())
                .genre(movieRequest.getGenre())
                .imdbId(movieRequest.getImdbId())
                .director(movieRequest.getDirector())
                .releaseYear(movieRequest.getReleaseYear())
                .type(movieRequest.getType())
                .build();
    }
    public static MovieResponse toMovieResponse(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .genre(movie.getGenre())
                .director(movie.getDirector())
                .imdbId(movie.getImdbId())
                .type(movie.getType())
                .year(String.valueOf(movie.getReleaseYear()))
                .poster(movie.getPoster())
                .ratingCount(movie.getRatingCount())
                .averageRating(movie.getAverageRating())
                .build();
    }

    public static Movie ToMovie(MovieResponse movieResponse) {
        return Movie.builder()
                .title(movieResponse.getTitle())
                .description(movieResponse.getDescription())
                .genre(movieResponse.getGenre())
                .director(movieResponse.getDirector())
                .imdbId(movieResponse.getImdbId())
                .releaseYear((movieResponse.getYear()))
                .poster(movieResponse.getPoster())
                .type(movieResponse.getType())
                .build();
    }
}
