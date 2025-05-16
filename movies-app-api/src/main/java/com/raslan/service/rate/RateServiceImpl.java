package com.raslan.service.rate;

import com.raslan.exception.RequestValidationException;
import com.raslan.exception.ResourceNotFoundException;
import com.raslan.mapper.MovieMapper;
import com.raslan.model.Movie;
import com.raslan.model.Rate;
import com.raslan.model.User;
import com.raslan.repository.MovieRepository;
import com.raslan.repository.RateRepository;
import com.raslan.repository.UserRepository;
import com.raslan.service.Movie.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RateServiceImpl implements RateService {
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final RateRepository rateRepository;
    private final MovieService movieService;

    @Override
    @Transactional
    public Map<String, Object> addRate(Integer userId, Integer movieId, Integer rateValue) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (rateRepository.existsByUserAndMovie(user, movie)) {
            throw new RequestValidationException("User has already rated this movie");
        }

        movieService.updateAverageRate(movie, rateValue);

        Rate rate = Rate.builder().user(user).movie(movie).rateValue(rateValue).build();

        rateRepository.save(rate);

        return Map.of(
                "message", "Rate added successfully",
                "updatedMovie", MovieMapper.toMovieResponse(movie)
        );

    }

    @Override
    public Map<String, Object> getRate(Integer userId, Integer movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Optional<Rate> rate = rateRepository.findByUserAndMovie(user,movie) ;
        return rate.<Map<String, Object>>map(value -> Map.of(
                "message", "Rate retrieved successfully",
                "value", value.getRateValue()
        )).orElseGet(() -> Map.of(
                "message", "User has not rated this movie",
                "value", 0
        ));
    }
}
