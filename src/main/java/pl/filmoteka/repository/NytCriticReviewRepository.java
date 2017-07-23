package pl.filmoteka.repository;

import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.model.integration.NytCriticReview;

import java.util.List;

/**
 * Custom repository for New York Times critic's review for a movie.
 */
public interface NytCriticReviewRepository {
    List<NytCriticReview> findByMovieName(String name) throws InvalidApplicationConfigurationException;
}
