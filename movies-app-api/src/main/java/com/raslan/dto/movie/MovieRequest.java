package com.raslan.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieRequest {
    private String title;
    private String description;
    private String poster;
    private String genre;
    private String director;
    private String imbdId;
    private Integer releaseYear;
    private String type;
}
