package com.raslan.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String poster ;

    private String genre;

    @Column(nullable = false)
    private String director;

    @Column(nullable = false)
    private String imbdId;

    @Column(name = "release_year", nullable = false)
    private Integer releaseYear;

    private String type ;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Rate> rates ;
    public Movie() {
        rates = new ArrayList<>() ;
    }
}
