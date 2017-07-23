package pl.filmoteka.repository;

import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.model.integration.Showtime;

import java.util.List;

/**
 * Custom repository for show times.
 */
public interface ShowtimeRepository {

    List<Showtime> findShowtimesByCinemaId(Long id) throws InvalidApplicationConfigurationException;
}
