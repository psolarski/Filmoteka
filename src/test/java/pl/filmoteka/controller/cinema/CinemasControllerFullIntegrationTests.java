package pl.filmoteka.controller.cinema;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import pl.filmoteka.AuthorizedTestsBase;
import pl.filmoteka.model.Director;
import pl.filmoteka.model.Movie;
import pl.filmoteka.model.integration.Cinema;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CinemasControllerFullIntegrationTests extends AuthorizedTestsBase {

    private String validLondonPostcode = "EC1A1AA";
    private String actuallyPlayedNowMovieName = "Dunkirk";

    @Test
    public void listOfCinemasInAreaWhileValidPostcodeGiven() {
        ResponseEntity<Cinema[]> response =
                testRestTemplate.getForEntity("/api/v1/cinemas/" + validLondonPostcode, Cinema[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().isNotEmpty();
    }

    @Test
    public void showsCinemasThatArePlayingChosenMovieToday() {
        Director director = new Director("name", "surname", "nationality");
        director = testRestTemplateAsAdmin.postForEntity("/api/v1/directors/create", director, Director.class)
                .getBody();
        Movie movie = new Movie(actuallyPlayedNowMovieName, 1, "genre", LocalDate.now(), "lang");
        movie.setDirector(director);
        movie = testRestTemplateAsAdmin.postForEntity("/api/v1/movies/create", movie, Movie.class).getBody();
        ResponseEntity<Cinema[]> response = testRestTemplate
                .getForEntity("/api/v1/cinemas/" + validLondonPostcode + "/movie/" + movie.getId(), Cinema[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull().isNotEmpty();
    }
}
