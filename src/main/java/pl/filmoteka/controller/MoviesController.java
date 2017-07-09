package pl.filmoteka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.filmoteka.model.Movie;
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
}
