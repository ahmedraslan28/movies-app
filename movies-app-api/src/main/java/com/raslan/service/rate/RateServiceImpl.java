package com.raslan.service.rate;

import com.raslan.dto.rate.RateRequest;
import com.raslan.exception.RequestValidationException;
import com.raslan.exception.ResourceNotFoundException;
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

@Service
@RequiredArgsConstructor
public class RateServiceImpl implements RateService {
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final RateRepository rateRepository;
    private final MovieService movieService;

    @Override
    @Transactional
    public Map<String, Object> addRate(RateRequest rateRequest) {
        Movie movie = movieRepository.findById(rateRequest.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        User user = userRepository.findById(rateRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (rateRepository.existsByUserAndMovie(user, movie)) {
            throw new RequestValidationException("User has already rated this movie");
        }


        movieService.updateAverageRate(movie, rateRequest.getRate());

        Rate rate = Rate.builder().user(user).movie(movie).rateValue(rateRequest.getRate()).build();

        rateRepository.save(rate);

        return Map.of(
                "message", "Rate added successfully",
                "rateValue", movie.getAverageRating()
        );

    }
}
