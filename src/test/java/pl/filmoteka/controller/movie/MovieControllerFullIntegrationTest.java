package pl.filmoteka.controller.movie;

import com.jayway.restassured.path.json.JsonPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import pl.filmoteka.model.Director;
import pl.filmoteka.model.Movie;

import java.time.LocalDate;
import java.util.List;

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
}
