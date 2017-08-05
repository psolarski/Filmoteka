package pl.filmoteka.controller.movie;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import pl.filmoteka.AuthorizedTestsBase;
import pl.filmoteka.model.Director;
import pl.filmoteka.model.Movie;
import pl.filmoteka.model.Rating;
import pl.filmoteka.repository.DirectorRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-end test for MovieController.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovieControllerFullIntegrationTest extends AuthorizedTestsBase {

    @Value("${test.db.initializer.movies.size}")
    private Integer moviesSize;

    @Autowired
    private DirectorRepository directorRepository;

    @Value("${best.rated.movie.limit}")
    private int filmLimit;

    private Director director;

    @Before
    public void init() {
        if (director == null) {
            director = new Director(
                    "MovieControllerFullIntegrationTestDirector",
                    "surname",
                    "someNationality"
            );
            director = directorRepository.saveAndFlush(director);
        }
    }

    @Test
    public void getAllMovies() {
        ResponseEntity<Movie[]> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/movies/all", Movie[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().isNotEmpty();
    }

    @Test
    public void getMoviesByName() {
        ResponseEntity<Movie[]> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/movies/name?name=name2", Movie[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    public void getMoviesByGenre() {
        ResponseEntity<Movie[]> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/movies/genre?genre=genre1", Movie[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    public void createMovie() {
        Movie movie = new Movie(
                "createMovieTest",
                50,
                "someGenre",
                LocalDate.now(),
                "Polish"
        );
        movie.setDirector(director);

        ResponseEntity<Movie> responseOnCreated = testRestTemplateAsAdmin
                .postForEntity("/api/v1/movies/create", movie, Movie.class);
        assertThat(responseOnCreated.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(movie.equals(responseOnCreated.getBody()));
    }

    @Test
    public void deleteMovie() {
        // First, create a movie
        Movie movie = new Movie(
                "deleteMovieTest",
                50,
                "someGenre",
                LocalDate.now(),
                "Polish"
        );
        movie.setDirector(director);

        ResponseEntity<Movie> responseOnCreated = testRestTemplateAsAdmin
                .postForEntity("/api/v1/movies/create", movie, Movie.class);
        assertThat(responseOnCreated.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Then delete it
        testRestTemplateAsAdmin
                .delete("/api/v1/movies/delete?id=" + responseOnCreated.getBody().getId());

        ResponseEntity<Movie[]> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/movies/all", Movie[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().isNotEmpty();
        Arrays.stream(response.getBody()).forEach(m -> assertThat(m.getName()).isNotEqualTo("deleteMovieTest"));
    }

    @Test
    public void updateMovie() {
        // First, create a movie
        Movie movie = new Movie(
                "updateMovieTest",
                50,
                "someGenre",
                LocalDate.now(),
                "Polish"
        );
        movie.setDirector(director);

        ResponseEntity<Movie> responseOnCreated = testRestTemplateAsAdmin
                .postForEntity("/api/v1/movies/create", movie, Movie.class);
        assertThat(responseOnCreated.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Check whether the application properly stored the movie
        ResponseEntity<Movie[]> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/movies/all", Movie[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().isNotEmpty();
        assertThat(Arrays.stream(response.getBody()).anyMatch(m -> m.getName().equals("updateMovieTest"))).isTrue();

        // Update the movie
        movie = responseOnCreated.getBody();
        movie.setName("updateMovieTestNewName");

        HttpEntity<Movie> httpEntity = new HttpEntity<>(movie, new HttpHeaders());
        ResponseEntity<Movie> responseOnUpdated = testRestTemplateAsAdmin
                .exchange("/api/v1/movies/update/" + movie.getId(), HttpMethod.PUT, httpEntity, Movie.class);
        assertThat(responseOnUpdated.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Get list of all movies to be sure
        ResponseEntity<Movie[]> responseAfterUpdate = testRestTemplate
                .getForEntity("/api/v1/movies/all", Movie[].class);

        assertThat(responseAfterUpdate.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseAfterUpdate.getBody()).isNotNull().isNotEmpty();
        assertThat(Arrays.stream(responseAfterUpdate.getBody())
                .anyMatch(m -> m.getName().equals("updateMovieTestNewName"))).isTrue();
    }

    @Test
    public void receiveNBestRatedMovies() {
        for (int i = 1; i <= 9; i++) {
            Movie movie = new Movie(
                    "MovieTestNameForRating" + i,
                    100 + i * 3,
                    "MovieTestGenreForRating" + i,
                    LocalDate.now().minusWeeks(i * 2),
                    "English");
            movie.setDirector(director);

            Set<Rating> ratings = new HashSet<>();
            ratings.add(new Rating(movie, i));
            ratings.add(new Rating(movie, i + 1));
            movie.setRatings(ratings);

            ResponseEntity<Movie> responseOnCreated = testRestTemplateAsAdmin
                    .postForEntity("/api/v1/movies/create", movie, Movie.class);
            assertThat(responseOnCreated.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(movie.equals(responseOnCreated.getBody()));
        }

        ResponseEntity<List<Movie>> responseEntity = testRestTemplateAsAdmin.exchange(
                "/api/v1/movies/rating/" + filmLimit,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {
                }
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody()).isNotNull().isNotEmpty().hasSize(filmLimit);
    }
}
