package com.raslan.service.Movie;

import com.raslan.dto.movie.MovieRequest;
import com.raslan.dto.movie.MovieResponse;
import com.raslan.dto.movie.OmdbSearchResponse;
import com.raslan.model.Movie;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface MovieService {

    public OmdbSearchResponse getMoviesFromOMDB(String searchParam, int page);

    public MovieResponse getOmdbMovie(String id);

    Map<String, Object> getAllMovies(String title, Integer page, Integer size);

    MovieResponse getMovie(Integer id);

    List<MovieResponse> addMovies(@RequestBody List<String> imdbIds);

    MovieResponse addMovie(@RequestBody String imdbId);

    void deleteMovies(@RequestBody List<Integer> moviesIds);

    void deleteMovie(@RequestBody Integer moviesIds);

    void updateAverageRate(Movie movie, Integer rateValue);
}
