package pl.filmoteka.repository;

import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.model.integration.NytCriticReview;

import java.util.List;

public interface NytCriticReviewRepository {
    List<NytCriticReview> findByMovieName(String name) throws InvalidApplicationConfigurationException;
}
