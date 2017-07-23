package pl.filmoteka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.exception.InvalidResourceRequestedException;
import pl.filmoteka.model.integration.Cinema;
import pl.filmoteka.service.CinemaService;

import java.util.Set;

/**
 * REST controller for manipulating data about cinemas. Works only for cinemas in UK and UK's postcodes!
 */
@RestController
@RequestMapping("api/v1/cinemas/")
public class CinemasController {

    @Autowired
    private CinemaService cinemaService;

    /**
     * Find all cinemas in area with given postcode in UK.
     *
     * @param postcode Valid UK's postcode
     * @return Response containing set of cinemas in chosen area and http status code
     */
    @RequestMapping(value = "{postcode}", method = RequestMethod.GET)
    public ResponseEntity<Set<Cinema>> findCinemasByPostcode(@PathVariable("postcode") String postcode) {
        try {
            return new ResponseEntity<>(cinemaService.findCinemasByPostcode(postcode), HttpStatus.OK);

        } catch (InvalidApplicationConfigurationException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * List all cinemas in area given by postcode that are playing today chosen movie, represented by its ID.
     *
     * @param postcode Valid UK's postcode
     * @param movieId  Chosen movie's ID
     * @returnResponse containing set of cinemas in chosen area that are plyaing today selected movie
     * and http status code
     */
    @RequestMapping(value = "{postcode}/movie/{movie_id}", method = RequestMethod.GET)
    public ResponseEntity<Set<Cinema>> findCinemasWithChosenMovieByPostcode(@PathVariable("postcode") String postcode, @PathVariable("movie_id") Long movieId) {
        try {
            return new ResponseEntity<>(cinemaService.findCinemasWithChosenMovieByPostcode(movieId, postcode), HttpStatus.OK);

        } catch (InvalidApplicationConfigurationException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (InvalidResourceRequestedException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
