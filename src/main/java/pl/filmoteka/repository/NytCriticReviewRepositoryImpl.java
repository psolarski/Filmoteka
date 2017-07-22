package pl.filmoteka.repository;

import org.apache.http.client.utils.URIBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.model.integration.NytCriticReview;
import pl.filmoteka.model.integration.NytResponse;

import java.net.URISyntaxException;
import java.util.List;

@Repository
public class NytCriticReviewRepositoryImpl implements NytCriticReviewRepository {

    // Logger
    final static Logger logger = Logger.getLogger(NytCriticReviewRepositoryImpl.class);

    @Value("${api.key.nyt}")
    private String nytApiKey;

    @Value("${api.url.nyt.review}")
    private String nytApiUrl;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Znajdź wystawione przez krytyków recenzje dla wybranego filmu na podstawie jego tytułu.
     *
     * @param name Tytuł wybranego filmu
     * @return Lista recenzji krytyków, przypisana do wybranego filmu
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
            logger.error("Invalid URI", e);

        } catch (HttpClientErrorException e) {
            logger.error("Connection error", e);
        }

        throw new InvalidApplicationConfigurationException();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
