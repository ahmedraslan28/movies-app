package com.raslan.controller;

import com.raslan.dto.movie.MovieRequest;
import com.raslan.dto.movie.MovieResponse;
import com.raslan.service.Movie.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/movies")
@RequiredArgsConstructor
public class MovieController {
    // get all movies api from external api using search param
    //    // patch add movies to database
    //        // select the movies you want
    //        // the body will be list of imdb id's
    //    // patch delete movies from database
    //    // add movie to database
    //    // delete movie from database
    //    // search movie by title in the database
    //    // get all movies with
    private final MovieService movieService;

    @GetMapping("/omdb")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getMoviesFromOMDB(@RequestParam(required = true) String searchParam,
                                               @RequestParam(defaultValue = "1") int page) {

        Object response = movieService.getMoviesFromOMDB(searchParam, page);


        return ResponseEntity.ok(response);
    }

    @GetMapping("/omdb/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getOmdbMovie(@PathVariable String id) {

        Object response = movieService.getOmdbMovie(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<MovieResponse>> addMovies(@RequestBody List<MovieRequest> movies) {
        List<MovieResponse> response = movieService.moviesPatchAdd(movies);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteMovies(@RequestBody List<Integer> moviesIds) {
        movieService.deleteMovies(moviesIds);
        return ResponseEntity.ok(Map.of("message", "Movies deleted successfully"));
    }


    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> getMovie(@PathVariable Integer id) {
        return ResponseEntity.ok(movieService.getMovie(id));
    }

    @GetMapping("")
    public ResponseEntity<List<MovieResponse>> getAllMovies(@RequestParam(required = false) String title,
                                                            @RequestParam(defaultValue = "1") Integer page,
                                                            @RequestParam(defaultValue = "5") Integer size) {
        List<MovieResponse> movies = movieService.getAllMovies(title, page - 1, size);
        return ResponseEntity.ok(movies);
    }


}
