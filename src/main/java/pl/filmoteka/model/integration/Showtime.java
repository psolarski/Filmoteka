package pl.filmoteka.model.integration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Class holding information about a single movie's showtime from external API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Showtime {

    private String title;

    @JsonProperty("time")
    private List<String> time;

    @JsonIgnore
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }
}
