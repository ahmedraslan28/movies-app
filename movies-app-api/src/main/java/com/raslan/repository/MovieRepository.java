package com.raslan.repository;

import com.raslan.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    boolean existsByImdbId(String imdbId);


}
