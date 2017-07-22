package pl.filmoteka.model.integration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NytResponse {

    @JsonProperty("results")
    private List<NytCriticReview> reviews = new ArrayList<>();

    public NytResponse() {}

    public NytResponse(List<NytCriticReview> reviews) {
        this.reviews = reviews;
    }

    public List<NytCriticReview> getReviews() {
        return reviews;
    }
}
