package pl.filmoteka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.exception.InvalidExternalApiResponseException;
import pl.filmoteka.exception.InvalidResourceRequestedException;
import pl.filmoteka.model.Movie;
import pl.filmoteka.model.integration.MusicAlbum;
import pl.filmoteka.model.integration.NytCriticReview;
import pl.filmoteka.model.integration.ProductList;
import pl.filmoteka.service.MovieService;
import pl.filmoteka.service.MusicAlbumService;
import pl.filmoteka.service.ProductService;

import java.util.List;

/**
 * Created by Piotr on 15.04.2017.
 */
@RestController
@RequestMapping("api/v1/movies/")
public class MoviesController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MusicAlbumService musicAlbumService;

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

    @RequestMapping(value = "{movie_id}/products", method = RequestMethod.GET)
    public ResponseEntity<ProductList> findProductsByMovie(@PathVariable("movie_id") Long id) {
        try {
            return new ResponseEntity<>(productService.findProductsByMovie(id), HttpStatus.OK);

        } catch (InvalidApplicationConfigurationException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (InvalidResourceRequestedException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        } catch (InvalidExternalApiResponseException e) {
            return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
        }
    }

    @RequestMapping(value = "{movie_id}/soundtrack", method = RequestMethod.GET)
    public ResponseEntity<List<MusicAlbum>> findMusicAlbumByMovieId(@PathVariable("movie_id") Long id) {
        try {
            return new ResponseEntity<>(musicAlbumService.findMusicAlbumByMovieId(id), HttpStatus.OK);

        } catch (InvalidResourceRequestedException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        } catch (InvalidExternalApiResponseException e) {
            return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);

        } catch (InvalidApplicationConfigurationException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "rating/{filmLimit}", method = RequestMethod.GET)
    public ResponseEntity<List<Movie>> findNRatedMovies(@PathVariable("filmLimit") int filmLimit) {
        return new ResponseEntity<>(movieService.findNBestRatedMovies(filmLimit), HttpStatus.OK);
    }
}
