package pl.filmoteka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.exception.InvalidExternalApiResponseException;
import pl.filmoteka.exception.InvalidResourceRequestedException;
import pl.filmoteka.model.Movie;
import pl.filmoteka.model.User;
import pl.filmoteka.model.integration.MusicAlbum;
import pl.filmoteka.model.integration.NytCriticReview;
import pl.filmoteka.model.integration.ProductList;
import pl.filmoteka.service.*;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Piotr on 15.04.2017.
 */
@RestController
@RequestMapping("api/v1/movies/")
public class MoviesController {

    // Logger
    final static Logger logger = Logger.getLogger(MoviesController.class.getName());

    @Autowired
    private MovieService movieService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MusicAlbumService musicAlbumService;

    @Autowired
    private MovieRecommenderService movieRecommenderService;

    @Autowired
    private TheMovieDbService theMovieDbService;

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

    @RequestMapping(value = "recommendations", method = RequestMethod.GET)
    public ResponseEntity<List<Movie>> recommendMovies() {
        String loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            return new ResponseEntity(movieRecommenderService.getRecommendations(loggedUsername), HttpStatus.OK);

        } catch (InvalidApplicationConfigurationException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Add chosen movie to user's watched movies list.
     *
     * @param id Chosen movie's ID
     * @return Response with updated user information
     */
    @RequestMapping(value = "{movie_id}/watched", method = RequestMethod.GET)
    public ResponseEntity<User> addMovieToWatched(@PathVariable("movie_id") Long id) {
        String loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            return new ResponseEntity<>(movieService.addMovieToWatched(loggedUsername, id), HttpStatus.OK);

        } catch (InvalidApplicationConfigurationException e) {
            logger.severe("Invalid logged in user");

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (InvalidResourceRequestedException e) {
            logger.severe("There's no user with requested ID");

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "{movie_id}/assign/actor/{actor_id}", method = RequestMethod.GET)
    public ResponseEntity<Movie> assignActorToMovie(@PathVariable("movie_id") Long movieId, @PathVariable("actor_id") Long actorId) {
        try {
            return new ResponseEntity<>(movieService.assignActorToMovie(movieId, actorId), HttpStatus.OK);

        } catch (InvalidResourceRequestedException e) {
            logger.severe("There's no movie or actor with requested ID");

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "{movie_id}/assign/director/{director_id}", method = RequestMethod.GET)
    public ResponseEntity<Movie> assignDirectorToMovie(@PathVariable("movie_id") Long movieId, @PathVariable("director_id") Long directorId) {
        try {
            return new ResponseEntity<>(movieService.assignDirectorToMovie(movieId, directorId), HttpStatus.OK);

        } catch (InvalidResourceRequestedException e) {
            logger.severe("There's no movie or director with requested ID");

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Integration with themoviedb.org
     */
    @RequestMapping(value = "movieFromMovieDb/{movieTitle}", method = RequestMethod.GET)
    public ResponseEntity receiveMoviesFromMovieDb(@PathVariable("movieTitle") String movieTitle) {

            return new ResponseEntity<>(theMovieDbService.findMovieInformation(movieTitle), HttpStatus.OK);
    }
}
