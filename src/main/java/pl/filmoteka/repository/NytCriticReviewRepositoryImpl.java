package pl.filmoteka.repository;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.model.integration.NytCriticReview;
import pl.filmoteka.model.integration.NytResponse;

import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Custom repository implementation for New York Times critic's review for a movie.
 */
@Repository
public class NytCriticReviewRepositoryImpl implements NytCriticReviewRepository {

    // Logger
    final static Logger logger = Logger.getLogger(NytCriticReviewRepositoryImpl.class.getName());

    @Value("${api.key.nyt}")
    private String nytApiKey;

    @Value("${api.url.nyt.review}")
    private String nytApiUrl;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Find NYTimes critics' reviews for movie with given title.
     *
     * @param name Name of chosen movie
     * @return List of critics' reviews
     * @throws InvalidApplicationConfigurationException Dane połączenia z API są nieprawidłowe
     */
    @Override
    public List<NytCriticReview> findByMovieName(String name) throws InvalidApplicationConfigurationException {
        try {
            URIBuilder uri = new URIBuilder(nytApiUrl);
            uri.addParameter("api-key", nytApiKey).addParameter("query", name);

            NytResponse response = restTemplate.getForObject(uri.build(), NytResponse.class);

            return response.getReviews();

        } catch (URISyntaxException e) {
            logger.severe("Invalid URI");

        } catch (HttpClientErrorException e) {
            logger.severe("Connection error");
        }

        throw new InvalidApplicationConfigurationException();
    }
}
