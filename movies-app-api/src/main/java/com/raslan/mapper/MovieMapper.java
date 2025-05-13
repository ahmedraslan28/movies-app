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
                .imbdId(movieRequest.getImbdId())
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
                .build();
    }
}
