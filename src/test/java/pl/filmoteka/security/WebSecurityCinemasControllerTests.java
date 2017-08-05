package pl.filmoteka.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import pl.filmoteka.AuthorizedTestsBase;
import pl.filmoteka.model.integration.Cinema;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSecurityCinemasControllerTests extends AuthorizedTestsBase {

    private String fakePostcode = "dontsendme";

    // CinemasController - get cinemas list by postcode
    @Test
    public void ensureThatGuestIsAbleToFindCinemasByPostcode() {
        ResponseEntity<Cinema[]> response = testRestTemplate.getForEntity(
                "/api/v1/cinemas/" + fakePostcode,
                Cinema[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatUserIsAbleToFindCinemasByPostcode() {
        ResponseEntity<Cinema[]> response = testRestTemplateAsUser
                .getForEntity("/api/v1/cinemas/" + fakePostcode, Cinema[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToFindCinemasByPostcode() {
        ResponseEntity<Cinema[]> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/cinemas/" + fakePostcode, Cinema[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // CinemasController - get showtimes in cinemas
    @Test
    public void ensureThatGuestIsAbleToFindShowtimesByMovie() {
        ResponseEntity<Cinema[]> response = testRestTemplate.getForEntity(
                "/api/v1/cinemas/" + fakePostcode + "/movie/5",
                Cinema[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatUserIsAbleToFindShowtimesByMovie() {
        ResponseEntity<Cinema[]> response = testRestTemplateAsUser
                .getForEntity("/api/v1/cinemas/" + fakePostcode + "/movie/5", Cinema[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToFindShowtimesByMovie() {
        ResponseEntity<Cinema[]> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/cinemas/" + fakePostcode + "/movie/5", Cinema[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
