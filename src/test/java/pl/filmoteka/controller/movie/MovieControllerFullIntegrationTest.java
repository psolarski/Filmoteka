package pl.filmoteka.controller.movie;

import com.jayway.restassured.path.json.JsonPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import pl.filmoteka.model.Director;
import pl.filmoteka.model.Movie;
import pl.filmoteka.model.Rating;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-end test for MovieController.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovieControllerFullIntegrationTest {

    @Value("${test.db.initializer.movies.size}")
    private Integer moviesSize;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Value("${best.rated.movie.limit}")
    private int filmLimit;

    @Test
    public void getAllMovies() {
        ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity("/api/v1/movies/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        assertThat((List<?>) path.get("name")).isNotNull().isNotEmpty().hasSize(moviesSize);
    }

    @Test
    public void getMoviesByName() {
        ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity("/api/v1/movies/name?name=name2", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        assertThat((List<?>) path.get("name")).isNotNull().isNotEmpty()
                .hasSize(1);
    }

    @Test
    public void getMoviesByGenre() {
        ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity("/api/v1/movies/genre?genre=genre1", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        assertThat((List<?>) path.get("name")).isNotNull().isNotEmpty()
                .hasSize(1);
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
        movie.setDirector(new Director("Katherine", "Travolta", "American"));

        ResponseEntity<Movie> responseOnCreated = testRestTemplate.withBasicAuth("admin", "password")
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
        movie.setDirector(new Director("John", "Troy", "American"));

        ResponseEntity<Movie> responseOnCreated = testRestTemplate.withBasicAuth("admin", "password")
                .postForEntity("/api/v1/movies/create", movie, Movie.class);
        assertThat(responseOnCreated.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Then delete it
        testRestTemplate.withBasicAuth("admin", "password")
                .delete("/api/v1/movies/delete?id=" + responseOnCreated.getBody().getId());

        ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity("/api/v1/movies/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        List <String> returnedNames = path.get("name");
        assertThat(returnedNames).isNotNull().isNotEmpty().doesNotContain("deleteMovieTest");
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
        movie.setDirector(new Director("John", "Troy", "American"));

        ResponseEntity<Movie> responseOnCreated = testRestTemplate.withBasicAuth("admin", "password")
                .postForEntity("/api/v1/movies/create", movie, Movie.class);
        assertThat(responseOnCreated.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Check whether the application properly stored the movie
        ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity("/api/v1/movies/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        List <String> returnedNames = path.get("name");
        assertThat(returnedNames).isNotNull().isNotEmpty().contains("updateMovieTest");

        // Update the movie
        movie = responseOnCreated.getBody();
        movie.setName("updateMovieTestNewName");

        HttpEntity<Movie> httpEntity = new HttpEntity<>(movie, new HttpHeaders());
        ResponseEntity<Movie> responseOnUpdated = testRestTemplate.withBasicAuth("admin", "password")
                .exchange("/api/v1/movies/update/" + movie.getId(), HttpMethod.PUT, httpEntity, Movie.class);
        assertThat(responseOnUpdated.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Get list of all movies to be sure
        ResponseEntity<String> responseAfterUpdate = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity("/api/v1/movies/all", String.class);

        assertThat(responseAfterUpdate.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath pathAfterUpdate = new JsonPath(responseAfterUpdate.getBody());

        List <String> returnedNamesAfterUpdate = pathAfterUpdate.get("name");
        assertThat(returnedNamesAfterUpdate).isNotNull().isNotEmpty().contains("updateMovieTestNewName");
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
            movie.setDirector(new Director("DirectorTestNameForRating" + i,
                    "DirectorTestSurnameForRating" + i,
                    "American"));

            Set<Rating> ratings = new HashSet<>();
            ratings.add(new Rating(movie, i));
            ratings.add(new Rating(movie, i + 1));
            movie.setRatings(ratings);

            ResponseEntity<Movie> responseOnCreated = testRestTemplate.withBasicAuth("admin", "password")
                    .postForEntity("/api/v1/movies/create", movie, Movie.class);
            assertThat(responseOnCreated.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(movie.equals(responseOnCreated.getBody()));
        }

        ResponseEntity<List<Movie>> responseEntity = testRestTemplate.withBasicAuth("admin", "password").exchange(
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
