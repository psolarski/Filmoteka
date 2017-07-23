package pl.filmoteka.model.integration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Class holding information about a single cinema from external API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cinema {

    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("address")
    private String address;

    @JsonProperty("url")
    private URL url;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Showtime> showtimes = new ArrayList<>();

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("venue_id")
    public void setId(Long id) {
        this.id = id;
    }

    public List<Showtime> getShowtimes() {
        return showtimes;
    }

    public void setShowtimes(List<Showtime> showtimes) {
        this.showtimes = showtimes;
    }
}
