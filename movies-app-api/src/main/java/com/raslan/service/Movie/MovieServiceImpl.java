package com.raslan.service.Movie;

import com.raslan.dto.movie.MovieRequest;
import com.raslan.dto.movie.MovieResponse;
import com.raslan.exception.RequestValidationException;
import com.raslan.exception.ResourceNotFoundException;
import com.raslan.mapper.MovieMapper;
import com.raslan.model.Movie;
import com.raslan.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final RestTemplate restTemplate;
    private final MovieRepository movieRepository;

    @Value("${omdb.api.url}")
    private String api_url;

    @Value("${omdb.api.key}")
    private String omdb_key;

    @Override
    public Object getMoviesFromOMDB(String searchParam, int page) {
        if (searchParam.isBlank()) {
            throw new RequestValidationException("Search param is required");
        }

        try {
            String url = api_url + "?apikey=" + omdb_key +
                    "&s=" + searchParam +
                    "&page=" + page;

            return restTemplate.getForObject(url, Object.class);
        } catch (Exception e) {
            throw new ResourceNotFoundException("no movies found");
        }
    }

    @Override
    public Object getOmdbMovie(String id) {
        try {
            String url = api_url + "?apikey=" + omdb_key + "&i=" + id;

            return restTemplate.getForObject(url, Object.class);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Movie not found");
        }
    }

    @Override
    @Transactional
    public List<MovieResponse> moviesPatchAdd(List<MovieRequest> moviesRequest) {
        if (moviesRequest == null || moviesRequest.isEmpty()) {
            throw new RequestValidationException("Can't add Empty list of movies");
        }
        List<Movie> movies = moviesRequest.stream().map(MovieMapper::toMovie).toList();
        movies = movieRepository.saveAll(movies);
        return movies.stream().map(MovieMapper::toMovieResponse).toList();
    }

    @Override
    @Transactional
    public void deleteMovies(List<Integer> moviesIds) {
        if (moviesIds == null || moviesIds.isEmpty()) {
            throw new RequestValidationException("Movies IDs are required");
        }

        List<Movie> existingMovies = movieRepository.findAllById(moviesIds);

        if (existingMovies.size() != moviesIds.size()) {
            throw new RequestValidationException("Some movie IDs do not exist");
        }

        movieRepository.deleteAll(existingMovies);
    }

    @Override
    public MovieResponse getMovie(Integer id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
        return MovieMapper.toMovieResponse(movie);
    }

    @Override
    public List<MovieResponse> getAllMovies(String title, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        if (title == null || title.isBlank()) {
            List<MovieResponse> m =  movieRepository.findAll(pageable).stream()
                    .map(MovieMapper::toMovieResponse)
                    .toList(); ;


            return m ;
        }

        return movieRepository.findByTitleContainingIgnoreCase(title, pageable)
                .stream()
                .map(MovieMapper::toMovieResponse)
                .toList();
    }
}
