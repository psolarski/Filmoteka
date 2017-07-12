package pl.filmoteka.security;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import pl.filmoteka.model.Actor;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSecurityActorControllerTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void ensureThatGuestIsAbleToReceiveActorsList() {
        ResponseEntity<String> allActorsResponse = testRestTemplate.
                                                    withBasicAuth("guest", "password").
                                                    getForEntity("/api/v1/actors/all", String.class);

        assertThat(allActorsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatUserIsAbleToReceiveActorsList() {
        ResponseEntity<String> allActorsResponse = testRestTemplate.
                                                    withBasicAuth("user", "password").
                                                    getForEntity("/api/v1/actors/all", String.class);

        assertThat(allActorsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToReceiveActorsList() {
        ResponseEntity<String> allActorsResponse = testRestTemplate.
                                                    withBasicAuth("admin", "password").
                                                    getForEntity("/api/v1/actors/all", String.class);

        assertThat(allActorsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatGuestIsNotAbleToCreateActor() {
        Actor actor = new Actor("Test", "test", "test");

        ResponseEntity<String> newActorResponse = testRestTemplate.
                                                    withBasicAuth("quest", "password").
                                                    postForEntity("/api/v1/actors/create", actor, String.class);

        assertThat(newActorResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsAbleToCreateNewActor() {
        Actor actor = new Actor("Test", "test", "test");

        ResponseEntity<String> newActorResponse = testRestTemplate.
                                                    withBasicAuth("user", "password").
                                                    postForEntity("/api/v1/actors/create", actor, String.class);

        assertThat(newActorResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToCreateNewActor() {
        Actor actor = new Actor("Test", "test", "test");

        ResponseEntity<String> newActorResponse = testRestTemplate.
                                                    withBasicAuth("admin", "password").
                                                    postForEntity("/api/v1/actors/create", actor, String.class);

        assertThat(newActorResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
