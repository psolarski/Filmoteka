package pl.filmoteka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.filmoteka.model.Movie;
import pl.filmoteka.repository.MovieRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Piotr on 15.04.2017.
 */
@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Transactional
    public List<Movie> findAllMovies() {
        return movieRepository.findAll();
    }

    @Transactional
    public List<Movie> findByName(String name) { return movieRepository.findByName(name); }

    @Transactional
    public List<Movie> findByGenre(String genre) { return movieRepository.findByGenre(genre); }

    @Transactional
    public Movie addNewMovie(Movie movie) {
        return movieRepository.saveAndFlush(movie);
    }

    @Transactional
    public void deleteMovie(Long id) {
        movieRepository.delete(id);
    }
}
