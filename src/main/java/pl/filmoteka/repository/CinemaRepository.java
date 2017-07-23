package pl.filmoteka.repository;

import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.model.integration.Cinema;

import java.util.Set;

/**
 * Custom repository for cinema.
 */
public interface CinemaRepository {

    Set<Cinema> findCinemasByPostcode(String postcode) throws InvalidApplicationConfigurationException;
}
