package pl.filmoteka.repository;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.model.integration.Cinema;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Custom repository implementation for cinema.
 */
@Repository
public class CinemaRepositoryImpl implements CinemaRepository {

    // Logger
    final static Logger logger = Logger.getLogger(CinemaRepositoryImpl.class.getName());

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.url.cinema.list}")
    private String cinemaListUrl;

    /**
     * Find all cinemas in area with given postcode in UK.
     *
     * @param postcode Valid postcode for UK
     * @return Set of cinemas in area
     * @throws InvalidApplicationConfigurationException Invalid configuration for external API
     */
    @Override
    public Set<Cinema> findCinemasByPostcode(String postcode) throws InvalidApplicationConfigurationException {
        try {
            URIBuilder uri = new URIBuilder(cinemaListUrl.replace("{postcode}", postcode));

            return new HashSet<>(Arrays.asList(restTemplate.getForObject(uri.build(), Cinema[].class)));

        } catch (URISyntaxException e) {
            logger.severe("Invalid URI");

        } catch (HttpClientErrorException e) {
            logger.severe("Connection error");
        }

        throw new InvalidApplicationConfigurationException();
    }
}
