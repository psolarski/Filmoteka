package pl.filmoteka.model.integration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NytCriticReview {

    @JsonProperty("display_title")
    private String displayTitle;

    @JsonProperty("critics_pick")
    private boolean criticPick;

    @JsonProperty("byline")
    private String author;

    @JsonProperty("summary_short")
    private String shortSummary;

    @JsonProperty("publication_date")
    private LocalDate publicationDate;

    @JsonProperty("link")
    private NytLink link;
}
