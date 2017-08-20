package pl.filmoteka.service;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.net.URISyntaxException;
import java.util.logging.Logger;

/**
 * Service used for integration with movie db.
 */
@Service
public class TheMovieDbService {

    // Logger
    final static Logger logger = Logger.getLogger(TheMovieDbService.class.getName());

    @Value("${api.themoviedb.key}")
    private String apiKey;

    @Value("${api.url.themoviedb.search}")
    private String movieDbUrl;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Calls MovieDB API for movies
     */
    public Object findMovieInformation(String movieTitle) {
        Object movieDbResponse = null;

        try {
            URIBuilder url = new URIBuilder(movieDbUrl);
            url.addParameter("api_key", apiKey);
            url.addParameter("query", movieTitle);

            movieDbResponse = restTemplate.getForEntity(url.build(), Object.class).getBody();
        } catch (URISyntaxException e) {
            logger.severe("Invalid URI");

        } catch (HttpClientErrorException e) {
            logger.severe("Connection error");
        }
        return movieDbResponse;
    }
}