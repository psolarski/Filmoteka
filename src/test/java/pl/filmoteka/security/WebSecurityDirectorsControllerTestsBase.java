package pl.filmoteka.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import pl.filmoteka.AuthorizedTestsBase;
import pl.filmoteka.model.Director;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSecurityDirectorsControllerTestsBase extends AuthorizedTestsBase {

    // DirectorController - get all directors
    @Test
    public void ensureThatGuestIsAbleToReceiveAllDirectorsList() {
        ResponseEntity<String> response = testRestTemplate.
                getForEntity("/api/v1/directors/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatUserIsAbleToReceiveAllDirectorsList() {
        ResponseEntity<String> response = testRestTemplateAsUser
                .getForEntity("/api/v1/directors/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToReceiveAllDirectorsList() {
        ResponseEntity<String> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/directors/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // DirectorController - get directors by name
    @Test
    public void ensureThatGuestIsAbleToReceiveDirectorsListByName() {
        ResponseEntity<String> response = testRestTemplate.
                getForEntity("/api/v1/directors/name?name=some", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatUserIsAbleToReceiveDirectorsListByName() {
        ResponseEntity<String> response = testRestTemplateAsUser
                .getForEntity("/api/v1/directors/name?name=some", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToReceiveDirectorsListByName() {
        ResponseEntity<String> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/directors/name?name=some", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // DirectorController - get directors by surname
    @Test
    public void ensureThatGuestIsAbleToReceiveDirectorsListBySurname() {
        ResponseEntity<String> response = testRestTemplate.
                getForEntity("/api/v1/directors/surname?surname=some", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatUserIsAbleToReceiveDirectorsListBySurname() {
        ResponseEntity<String> response = testRestTemplateAsUser
                .getForEntity("/api/v1/directors/surname?surname=some", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToReceiveDirectorsListBySurname() {
        ResponseEntity<String> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/directors/surname?surname=some", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // DirectorController - get directors by name or surname
    @Test
    public void ensureThatGuestIsAbleToReceiveDirectorsListByNameOrSurname() {
        ResponseEntity<String> response = testRestTemplate.
                getForEntity("/api/v1/directors/nameorsurname?name=some&surname=some", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatUserIsAbleToReceiveDirectorsListByNameOrSurname() {
        ResponseEntity<String> response = testRestTemplateAsUser
                .getForEntity("/api/v1/directors/nameorsurname?name=some&surname=some", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToReceiveDirectorsListByNameOrSurname() {
        ResponseEntity<String> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/directors/nameorsurname?name=some&surname=some", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // DirectorController - create director
    @Test
    public void ensureThatGuestIsNotAbleToCreateDirector() {
        Director director = new Director("Test", "test", "test");

        ResponseEntity<Director> response = testRestTemplate.
                postForEntity("/api/v1/directors/create", director, Director.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsNotAbleToCreateDirector() {
        Director director = new Director("Test", "test", "test");

        ResponseEntity<Director> response = testRestTemplateAsUser
                .postForEntity("/api/v1/directors/create", director, Director.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToCreateNewDirector() {
        Director director = new Director("Test", "test", "test");

        ResponseEntity<Director> response = testRestTemplateAsAdmin
                .postForEntity("/api/v1/directors/create", director, Director.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // DirectorController - delete director
    // We can just try to delete some nonexistent director
    @Test
    public void ensureThatGuestIsNotAbleToDeleteDirector() {
        ResponseEntity<Void> response = testRestTemplate
                .exchange("/api/v1/directors/delete?id=1", HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsNotAbleToDeleteDirector() {
        ResponseEntity<Void> response = testRestTemplateAsUser
                .exchange("/api/v1/directors/delete?id=1", HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToDeleteDirector() {
        Director director = new Director("Test", "test", "test");

        // Create director as admin to be sure
        ResponseEntity<Director> response = testRestTemplateAsAdmin
                .postForEntity("/api/v1/directors/create", director, Director.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        director = response.getBody();
        ResponseEntity<Void> responseOnDelete = testRestTemplateAsAdmin
                .exchange("/api/v1/directors/delete?id=" + director.getId(), HttpMethod.DELETE, null, Void.class);

        assertThat(responseOnDelete.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // DirectorController - update existing director
    @Test
    public void ensureThatGuestIsNotAbleToUpdateDirector() {
        Director director = new Director("Test", "test", "test");

        // Create director as admin to be sure
        ResponseEntity<Director> response = testRestTemplateAsAdmin
                .postForEntity("/api/v1/directors/create", director, Director.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // We don't have to change director - just test permissions
        director = response.getBody();
        HttpEntity<Director> httpEntity = new HttpEntity<>(director, new HttpHeaders());
        ResponseEntity<Director> responseOnUpdated = testRestTemplate
                .exchange("/api/v1/directors/update/" + director.getId(), HttpMethod.PUT, httpEntity, Director.class);

        assertThat(responseOnUpdated.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsNotAbleToUpdateDirector() {
        Director director = new Director("Test", "test", "test");

        // Create director as admin to be sure
        ResponseEntity<Director> response = testRestTemplateAsAdmin
                .postForEntity("/api/v1/directors/create", director, Director.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // We don't have to change director - just test permissions
        director = response.getBody();
        HttpEntity<Director> httpEntity = new HttpEntity<>(director, new HttpHeaders());
        ResponseEntity<Director> responseOnUpdated = testRestTemplateAsUser
                .exchange("/api/v1/directors/update/" + director.getId(), HttpMethod.PUT, httpEntity, Director.class);

        assertThat(responseOnUpdated.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToUpdateDirector() {
        Director director = new Director("Test", "test", "test");

        // Create director as admin to be sure
        ResponseEntity<Director> response = testRestTemplateAsAdmin
                .postForEntity("/api/v1/directors/create", director, Director.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // We don't have to change director - just test permissions
        director = response.getBody();
        HttpEntity<Director> httpEntity = new HttpEntity<>(director, new HttpHeaders());
        ResponseEntity<Director> responseOnUpdated = testRestTemplateAsAdmin
                .exchange("/api/v1/directors/update/" + director.getId(), HttpMethod.PUT, httpEntity, Director.class);

        assertThat(responseOnUpdated.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
