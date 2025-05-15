package com.raslan.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieResponse {
    private int id;
    private String title;
    private String description;
    private String genre;
    private String director;
    private String imdbId;
    private String year;
    private String poster;
    private String type ;
    private Integer ratingCount ;
    private Double averageRating ;
}
