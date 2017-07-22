package pl.filmoteka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.model.Movie;
import pl.filmoteka.model.integration.NytCriticReview;
import pl.filmoteka.service.MovieService;

import java.util.List;

/**
 * Created by Piotr on 15.04.2017.
 */
@RestController
@RequestMapping("api/v1/movies/")
public class MoviesController {

    @Autowired
    private MovieService movieService;

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public List<Movie> findAll() {
        return movieService.findAllMovies();
    }

    @RequestMapping(value = "name", method = RequestMethod.GET)
    public List<Movie> findByName(@RequestParam(value = "name") String name) {
        return movieService.findByName(name);
    }

    @RequestMapping(value = "genre", method = RequestMethod.GET)
    public List<Movie> findByGenre(@RequestParam(value = "genre") String genre) {
        return movieService.findByGenre(genre);
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Movie createMovie(@RequestBody Movie movie) {
        return movieService.addNewMovie(movie);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public void deleteMovie(@RequestParam(value = "id") Long id) {
        movieService.deleteMovie(id);
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Movie> updateDirector(@PathVariable("id") long id, @RequestBody Movie modifiedMovie) {
        // Get already stored movie with given id
        Movie storedMovie = movieService.find(id);

        if (storedMovie == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Explicitly set passed values
        storedMovie.setName(modifiedMovie.getName());
        storedMovie.setDirector(modifiedMovie.getDirector());
        storedMovie.setDuration(modifiedMovie.getDuration());
        storedMovie.setGenre(modifiedMovie.getGenre());
        storedMovie.setLanguage(modifiedMovie.getLanguage());
        storedMovie.setReleaseDate(modifiedMovie.getReleaseDate());

        Movie updatedEntity = movieService.updateMovie(storedMovie);

        return new ResponseEntity<>(updatedEntity, HttpStatus.OK);
    }

    @RequestMapping(value = "{id}/reviews", method = RequestMethod.GET)
    public ResponseEntity<List<NytCriticReview>> findCriticReviewsByMovieId(@PathVariable(value = "id") Long id) {
        try {
            return new ResponseEntity<>(movieService.findCriticReviewsByMovieId(id), HttpStatus.OK);

        } catch (InvalidApplicationConfigurationException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
