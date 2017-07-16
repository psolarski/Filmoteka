package pl.filmoteka.repository.Movie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import pl.filmoteka.model.Director;
import pl.filmoteka.model.Movie;
import pl.filmoteka.repository.MovieRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests responsible for movie repository
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class MovieRepositoryMethodTests {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    @Rollback
    public void testFindByName() {
        Movie movie = new Movie("TestMovie",
                                10,
                                "testGenre",
                                LocalDate.now(),
                                "polish");
        movie.setDirector(new Director("name", "surname", "American"));
        movieRepository.saveAndFlush(movie);

        List<Movie> movieList = movieRepository.findByName("TestMovie");
        assertThat(movieList).isNotEmpty()
                             .contains(movie)
                             .hasSize(1);
    }

    @Test
    @Rollback
    public void testFindByGenre() {
        Movie movie = new Movie("TestMovie",
                10,
                "testGenre",
                LocalDate.now(),
                "polish");
        movie.setDirector(new Director("name", "surname", "American"));
        movieRepository.saveAndFlush(movie);

        List<Movie> movieList = movieRepository.findByGenre("testGenre");
        assertThat(movieList).isNotEmpty()
                             .contains(movie)
                             .hasSize(1);
    }
}
