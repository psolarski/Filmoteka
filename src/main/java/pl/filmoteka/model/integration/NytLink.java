package pl.filmoteka.model.integration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NytLink {

    @JsonProperty("url")
    private URL url;

    @JsonProperty("suggested_link_text")
    private String urlText;
}
