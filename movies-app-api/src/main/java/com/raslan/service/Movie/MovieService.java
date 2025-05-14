package com.raslan.service.Movie;

import com.raslan.dto.movie.MovieRequest;
import com.raslan.dto.movie.MovieResponse;
import com.raslan.dto.movie.OmdbSearchResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface MovieService {

    public OmdbSearchResponse getMoviesFromOMDB(String searchParam, int page);

    public MovieResponse getOmdbMovie(String id);

    List<MovieResponse> moviesPatchAdd(List<MovieRequest> movies);

    List<MovieResponse> getAllMovies(String title, Integer page, Integer size);

    MovieResponse getMovie(Integer id);

    List<MovieResponse> addMovies(@RequestBody List<String> imdbIds);

    MovieResponse addMovie(@RequestBody String imdbId);

    void deleteMovies(@RequestBody List<Integer> moviesIds);

    void deleteMovie(@RequestBody Integer moviesIds);
}
