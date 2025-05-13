package com.raslan.service.Movie;

import com.raslan.dto.movie.MovieRequest;
import com.raslan.dto.movie.MovieResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface MovieService {

    public Object getMoviesFromOMDB(String searchParam, int page);

    public Object getOmdbMovie(String id) ;

    List<MovieResponse> moviesPatchAdd(List<MovieRequest> movies);

    void deleteMovies(@RequestBody List<Integer> moviesIds);

    MovieResponse getMovie(Integer id);

    List<MovieResponse> getAllMovies(String title, Integer page, Integer size);
}
