package pl.filmoteka.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import pl.filmoteka.AuthorizedTestsBase;
import pl.filmoteka.model.Rating;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSecurityRatingsControllerTests extends AuthorizedTestsBase {

    // RatingsController - get all ratings
    @Test
    public void ensureThatGuestIsAbleToGetAllRatings() {
        ResponseEntity<Rating[]> response = testRestTemplate.getForEntity("/api/v1/ratings/all", Rating[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatUserIsAbleToGetAllRatings() {
        ResponseEntity<Rating[]> response = testRestTemplateAsUser
                .getForEntity("/api/v1/ratings/all", Rating[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToGetAllRatings() {
        ResponseEntity<Rating[]> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/ratings/all", Rating[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // RatingsController - create rating
    @Test
    public void ensureThatGuestIsNotAbleToCreateRating() {
        ResponseEntity<Rating> response = testRestTemplate.postForEntity(
                "/api/v1/ratings/create",
                null,
                Rating.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsAbleToCreateRating() {
        Rating rating = new Rating(null, 10);
        ResponseEntity<Rating> response = testRestTemplateAsUser
                .postForEntity("/api/v1/ratings/create?movieId=4", rating, Rating.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToCreateRating() {
        Rating rating = new Rating(null, 10);
        ResponseEntity<Rating> response = testRestTemplateAsAdmin
                .postForEntity("/api/v1/ratings/create?movieId=4", rating, Rating.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // RatingsController - delete rating
    @Test
    public void ensureThatGuestIsNotAbleToDeleteRating() {
        ResponseEntity<Void> response = testRestTemplate
                .exchange("/api/v1/ratings/delete/1", HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsNotAbleToDeleteRating() {
        ResponseEntity<Void> response = testRestTemplateAsUser
                .exchange("/api/v1/ratings/delete/1", HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToDeleteRating() {
        ResponseEntity<Void> response = testRestTemplateAsAdmin
                .exchange("/api/v1/ratings/delete/1", HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // RatingsController - update rating
    @Test
    public void ensureThatGuestIsNotAbleToUpdateRating() {
        ResponseEntity<Void> response = testRestTemplate
                .exchange("/api/v1/ratings/update/0", HttpMethod.PUT, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsNotAbleToUpdateRating() {
        ResponseEntity<Void> response = testRestTemplateAsUser
                .exchange("/api/v1/ratings/update/0", HttpMethod.PUT, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToUpdateRating() {
        Rating rating = testRestTemplate.getForEntity("/api/v1/ratings/all", Rating[].class).getBody()[0];
        HttpEntity<Rating> httpEntity = new HttpEntity<>(rating, new HttpHeaders());
        ResponseEntity<Rating> response = testRestTemplateAsAdmin
                .exchange("/api/v1/ratings/update/" + rating.getId(), HttpMethod.PUT, httpEntity, Rating.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
