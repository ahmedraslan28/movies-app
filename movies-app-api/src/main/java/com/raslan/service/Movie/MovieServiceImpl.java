package com.raslan.service.Movie;

import com.raslan.dto.movie.MovieRequest;
import com.raslan.dto.movie.MovieResponse;
import com.raslan.dto.movie.OmdbMovieResponse;
import com.raslan.dto.movie.OmdbSearchResponse;
import com.raslan.exception.RequestValidationException;
import com.raslan.exception.ResourceNotFoundException;
import com.raslan.mapper.MovieMapper;
import com.raslan.model.Movie;
import com.raslan.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieServiceImpl implements MovieService {
    private final RestTemplate restTemplate;
    private final MovieRepository movieRepository;

    @Value("${omdb.api.url}")
    private String api_url;

    @Value("${omdb.api.key}")
    private String omdb_key;

    @Override
    public OmdbSearchResponse getMoviesFromOMDB(String searchParam, int page) {
        if (searchParam.isBlank()) {
            throw new RequestValidationException("Search param is required");
        }

        String url = api_url + "?apikey=" + omdb_key + "&s=" + searchParam + "&page=" + page;
        Map<String, Object> response = (Map<String, Object>) restTemplate.getForObject(url, Object.class);
        return OmdbSearchResponse.builder()
                .Search((List<OmdbMovieResponse>) response.get("Search"))
                .totalResults((String) response.get("totalResults"))
                .Response((String) response.get("Response"))
                .build();
    }

    @Override
    public MovieResponse getOmdbMovie(String id) {
        String url = api_url + "?apikey=" + omdb_key + "&i=" + id;
        Map<String, Object> response = (Map<String, Object>) restTemplate.getForObject(url, Object.class);
        if (response.size() == 2) {
            throw new ResourceNotFoundException((String) response.get("Error"));
        }
        return MovieResponse.builder()
                .title((String) response.get("Title"))
                .description((String) response.get("Plot"))
                .poster((String) response.get("Poster"))
                .genre((String) response.get("Genre"))
                .director((String) response.get("Director"))
                .imdbId((String) response.get("imdbID"))
                .year((String) response.get("Year"))
                .type((String) response.get("Type"))
                .build();
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
    public MovieResponse getMovie(Integer id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
        return MovieMapper.toMovieResponse(movie);
    }

    @Override
    public Map<String, Object> getAllMovies(String title, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        List<MovieResponse> moviesResponse;
        if (title == null || title.isBlank()) {
            moviesResponse = movieRepository.findAll(pageable).stream()
                    .map(MovieMapper::toMovieResponse)
                    .toList();
        } else {
            moviesResponse = movieRepository.findByTitleContainingIgnoreCase(title, pageable)
                    .stream()
                    .map(MovieMapper::toMovieResponse)
                    .toList();
        }
        return Map.of("movies", moviesResponse, "total", movieRepository.count());
    }

    @Override
    @Transactional
    public List<MovieResponse> addMovies(List<String> imdbIds) {
        List<Movie> movies = new ArrayList<>();

        for (String id : imdbIds) {
            if (movieRepository.existsByImdbId(id)) {
                continue;
            }

            MovieResponse movie = getOmdbMovie(id);

            movies.add(MovieMapper.ToMovie(movie));
        }
        movies = movieRepository.saveAll(movies);
        log.info(movies.toString());
        return movies.stream().map(MovieMapper::toMovieResponse).toList();
    }

    @Override
    public MovieResponse addMovie(String imdbId) {
        if (movieRepository.existsByImdbId(imdbId)) {
            return null;
        }

        MovieResponse movie = getOmdbMovie(imdbId);
        Movie movieEntity = MovieMapper.ToMovie(movie);
        movieRepository.save(movieEntity);
        return movie;
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
    public void deleteMovie(Integer movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new ResourceNotFoundException("Movie not found");
        }
        movieRepository.deleteById(movieId);
    }

    @Override
    public void updateAverageRate(Movie movie, Integer rateValue) {
        if (movie.getRatingCount() == null) {
            movie.setRatingCount(0);
            movie.setAverageRating(0.0);
        }
        Double totalRating = movie.getRatingCount() * movie.getAverageRating();
        movie.setRatingCount(movie.getRatingCount() + 1);
        totalRating += rateValue;
        movie.setAverageRating(totalRating / movie.getRatingCount());
        movieRepository.save(movie);
    }
}
