package pl.filmoteka.repository;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.model.integration.Showtime;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Custom repository implementation for show times.
 */
@Repository
public class ShowtimeRepositoryImpl implements ShowtimeRepository {

    // Logger
    final static Logger logger = Logger.getLogger(ShowtimeRepositoryImpl.class.getName());

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.url.cinema.showtimes}")
    private String cinemaShowtimesListUrl;

    /**
     * Get all show times for chosen cinema by its ID.
     *
     * @param id ID number of chosen venue
     * @return List of show times for chosen cinema
     * @throws InvalidApplicationConfigurationException Invalid configuration for external API
     */
    @Override
    public List<Showtime> findShowtimesByCinemaId(Long id) throws InvalidApplicationConfigurationException {
        try {
            URIBuilder uri = new URIBuilder(cinemaShowtimesListUrl.replace("{id}", id.toString()));

            return Arrays.asList(restTemplate.getForObject(uri.build(), Showtime[].class));

        } catch (URISyntaxException e) {
            logger.severe("Invalid URI");

        } catch (HttpClientErrorException e) {
            logger.severe("Connection error");
        }

        throw new InvalidApplicationConfigurationException();
    }
}
