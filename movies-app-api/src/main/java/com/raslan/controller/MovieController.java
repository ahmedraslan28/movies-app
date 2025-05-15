package com.raslan.controller;

import com.raslan.dto.movie.MovieRequest;
import com.raslan.dto.movie.MovieResponse;
import com.raslan.dto.movie.OmdbSearchResponse;
import com.raslan.service.Movie.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/omdb")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<OmdbSearchResponse> getMoviesFromOMDB(@RequestParam(required = true) String searchParam,
                                                                @RequestParam(defaultValue = "1") int page) {

        return ResponseEntity.ok(movieService.getMoviesFromOMDB(searchParam, page));
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<MovieResponse>> addMovies(@RequestBody List<String> imdbIds) {
        List<MovieResponse> response = movieService.addMovies(imdbIds);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addMovie")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MovieResponse> addMovie(@RequestBody String imdbid) {
        return ResponseEntity.ok(movieService.addMovie(imdbid));
    }




    @PostMapping("/deletePatch")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteMovies(@RequestBody List<Integer> moviesIds) {
        movieService.deleteMovies(moviesIds);
        return ResponseEntity.ok(Map.of("message", "Movies deleted successfully"));
    }

    @DeleteMapping("/{movieId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteMovie(@PathVariable Integer movieId) {
        movieService.deleteMovie(movieId);
        return ResponseEntity.ok(Map.of("message", "Movie deleted successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> getMovie(@PathVariable Integer id) {
        return ResponseEntity.ok(movieService.getMovie(id));
    }

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAllMovies(@RequestParam(required = false) String title,
                                                            @RequestParam(defaultValue = "1") Integer page,
                                                            @RequestParam(defaultValue = "5") Integer size) {
        return ResponseEntity.ok(movieService.getAllMovies(title, page, size));
    }


}
