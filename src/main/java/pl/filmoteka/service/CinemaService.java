package pl.filmoteka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.exception.InvalidResourceRequestedException;
import pl.filmoteka.model.Movie;
import pl.filmoteka.model.integration.Cinema;
import pl.filmoteka.repository.CinemaRepository;
import pl.filmoteka.repository.MovieRepository;
import pl.filmoteka.repository.ShowtimeRepository;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for cinema.
 */
@Service
public class CinemaService {

    @Autowired
    private CinemaRepository cinemaRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private MovieRepository movieRepository;

    /**
     * Find all cinemas in area with given postcode in UK.
     *
     * @param postcode Valid postcode for UK
     * @return Set of cinemas in area
     * @throws InvalidApplicationConfigurationException Invalid configuration for external API
     */
    public Set<Cinema> findCinemasByPostcode(String postcode) throws InvalidApplicationConfigurationException {
        return cinemaRepository.findCinemasByPostcode(postcode);
    }

    /**
     * Find movie show times in cinemas in area with given postcode in UK.
     *
     * @param movieId Movie's ID which show times will be searched for
     * @param postcode Area's postcode
     * @return Set of cinemas in area that play chosen movie
     * @throws InvalidApplicationConfigurationException Invalid configuration for external API
     * @throws InvalidResourceRequestedException Movie with chosen id doesn't exist
     */
    public Set<Cinema> findCinemasWithChosenMovieByPostcode(Long movieId, String postcode)
            throws InvalidApplicationConfigurationException, InvalidResourceRequestedException {

        Movie movie = movieRepository.findOne(movieId);

        if (movie == null) {
            throw new InvalidResourceRequestedException("There's no movie with id " + movieId);
        }

        Set<Cinema> cinemasInArea = cinemaRepository.findCinemasByPostcode(postcode);

        cinemasInArea.forEach(c -> {
            try {
                c.setShowtimes(showtimeRepository.findShowtimesByCinemaId(
                        c.getId()).stream().filter(
                        s -> s.getTitle().equals(movie.getName())
                        ).collect(Collectors.toList())
                );

            } catch (InvalidApplicationConfigurationException e) {
                // No idea what to do here - tried to .map this whole thing but I did't know how
                throw new RuntimeException();
            }
        });

        return cinemasInArea;
    }
}
