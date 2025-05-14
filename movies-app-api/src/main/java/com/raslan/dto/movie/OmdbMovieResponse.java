package com.raslan.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OmdbMovieResponse {
    private String Title;
    private String Year;
    private String imdbID;
    private String Type;
    private String Poster ;

}
