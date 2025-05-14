package com.raslan.dto.movie;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OmdbSearchResponse {
    @JsonProperty("Search")
    private List<OmdbMovieResponse> Search ;
    private String totalResults ;
    @JsonProperty("Response")
    private String Response ;
}
