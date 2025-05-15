package com.raslan.repository;

import com.raslan.model.Movie;
import com.raslan.model.Rate;
import com.raslan.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RateRepository extends JpaRepository<Rate,Integer> {
    boolean existsByUserAndMovie(User user, Movie movie);

    Optional<Rate> findByUserAndMovie(User user, Movie movie);
}
