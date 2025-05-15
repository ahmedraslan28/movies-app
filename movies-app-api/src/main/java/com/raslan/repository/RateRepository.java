package com.raslan.repository;

import com.raslan.model.Movie;
import com.raslan.model.Rate;
import com.raslan.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateRepository extends JpaRepository<Rate,Integer> {
    boolean existsByUserAndMovie(User user, Movie movie);
}
