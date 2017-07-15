package pl.filmoteka.security;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import pl.filmoteka.model.Actor;
import pl.filmoteka.model.Director;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSecurityActorControllerTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    // ActorController - get all actors
    @Test
    public void ensureThatGuestIsAbleToReceiveAllActorsList() {
        ResponseEntity<String> allActorsResponse = testRestTemplate.
                                                    getForEntity("/api/v1/actors/all", String.class);

        assertThat(allActorsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatUserIsAbleToReceiveAllActorsList() {
        ResponseEntity<String> allActorsResponse = testRestTemplate.
                                                    withBasicAuth("user", "password").
                                                    getForEntity("/api/v1/actors/all", String.class);

        assertThat(allActorsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToReceiveAllActorsList() {
        ResponseEntity<String> allActorsResponse = testRestTemplate.
                                                    withBasicAuth("admin", "password").
                                                    getForEntity("/api/v1/actors/all", String.class);

        assertThat(allActorsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // ActorController - get actors by name
    @Test
    public void ensureThatGuestIsAbleToReceiveActorsListByName() {
        ResponseEntity<String> allActorsResponse = testRestTemplate.
                                                    getForEntity("/api/v1/actors/name?name=some", String.class);

        assertThat(allActorsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatUserIsAbleToReceiveActorsListByName() {
        ResponseEntity<String> allActorsResponse = testRestTemplate.
                                                    withBasicAuth("user", "password").
                                                    getForEntity("/api/v1/actors/name?name=some", String.class);

        assertThat(allActorsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToReceiveActorsListByName() {
        ResponseEntity<String> allActorsResponse = testRestTemplate.
                                                    withBasicAuth("admin", "password").
                                                    getForEntity("/api/v1/actors/name?name=some", String.class);

        assertThat(allActorsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // ActorController - get actors by surname
    @Test
    public void ensureThatGuestIsAbleToReceiveActorsListBySurname() {
        ResponseEntity<String> allActorsResponse = testRestTemplate.
                                                    getForEntity("/api/v1/actors/surname?surname=some", String.class);

        assertThat(allActorsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatUserIsAbleToReceiveActorsListBySurname() {
        ResponseEntity<String> allActorsResponse = testRestTemplate.
                                                    withBasicAuth("user", "password").
                                                    getForEntity("/api/v1/actors/surname?surname=some", String.class);

        assertThat(allActorsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToReceiveActorsListBySurname() {
        ResponseEntity<String> allActorsResponse = testRestTemplate.
                                                    withBasicAuth("admin", "password").
                                                    getForEntity("/api/v1/actors/surname?surname=some", String.class);

        assertThat(allActorsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // ActorController - get actors by name or surname
    @Test
    public void ensureThatGuestIsAbleToReceiveActorsListByNameOrSurname() {
        ResponseEntity<String> allActorsResponse = testRestTemplate.
                getForEntity("/api/v1/actors/nameorsurname?name=some&surname=some", String.class);

        assertThat(allActorsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatUserIsAbleToReceiveActorsListByNameOrSurname() {
        ResponseEntity<String> allActorsResponse = testRestTemplate.
                withBasicAuth("user", "password").
                getForEntity("/api/v1/actors/nameorsurname?name=some&surname=some", String.class);

        assertThat(allActorsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToReceiveActorsListByNameOrSurname() {
        ResponseEntity<String> allActorsResponse = testRestTemplate.
                withBasicAuth("admin", "password").
                getForEntity("/api/v1/actors/nameorsurname?name=some&surname=some", String.class);

        assertThat(allActorsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // ActorController - create actor
    @Test
    public void ensureThatGuestIsNotAbleToCreateActor() {
        Actor actor = new Actor("Test", "test", "test");

        ResponseEntity<Actor> newActorResponse = testRestTemplate.
                                                    postForEntity("/api/v1/actors/create", actor, Actor.class);

        assertThat(newActorResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsNotAbleToCreateActor() {
        Actor actor = new Actor("Test", "test", "test");

        ResponseEntity<Actor> newActorResponse = testRestTemplate.
                                                    withBasicAuth("user", "password").
                                                    postForEntity("/api/v1/actors/create", actor, Actor.class);

        assertThat(newActorResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToCreateNewActor() {
        Actor actor = new Actor("Test", "test", "test");

        ResponseEntity<Actor> newActorResponse = testRestTemplate.
                                                    withBasicAuth("admin", "password").
                                                    postForEntity("/api/v1/actors/create", actor, Actor.class);

        assertThat(newActorResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // ActorController - delete actor
    // We can just try to delete some nonexistent actor
    @Test
    public void ensureThatGuestIsNotAbleToDeleteActor() {
        ResponseEntity<Void> response = testRestTemplate
                .exchange("/api/v1/actors/delete?id=1", HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsNotAbleToDeleteActor() {
        ResponseEntity<Void> response = testRestTemplate.withBasicAuth("user", "password")
                .exchange("/api/v1/actors/delete?id=1", HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToDeleteActor() {
        ResponseEntity<Void> response = testRestTemplate.withBasicAuth("admin", "password")
                .exchange("/api/v1/actors/delete?id=1", HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // ActorController - update existing actor
    @Test
    public void ensureThatGuestIsNotAbleToUpdateActor() {
        Actor actor = new Actor("Test", "Test", "Test");

        // Create actor as admin to be sure
        ResponseEntity<Actor> response = testRestTemplate.
                withBasicAuth("admin", "password").
                postForEntity("/api/v1/actors/create", actor, Actor.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // We don't have to change actor - just test permissions
        actor = response.getBody();
        HttpEntity<Actor> httpEntity = new HttpEntity<>(actor, new HttpHeaders());
        ResponseEntity<Actor> responseOnUpdated = testRestTemplate
                .exchange("/api/v1/actors/update/" + actor.getId(), HttpMethod.PUT, httpEntity, Actor.class);

        assertThat(responseOnUpdated.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsNotAbleToUpdateActor() {
        Actor actor = new Actor("Test", "Test", "Test");

        // Create actor as admin to be sure
        ResponseEntity<Actor> response = testRestTemplate.
                withBasicAuth("admin", "password").
                postForEntity("/api/v1/actors/create", actor, Actor.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // We don't have to change actor - just test permissions
        actor = response.getBody();
        HttpEntity<Actor> httpEntity = new HttpEntity<>(actor, new HttpHeaders());
        ResponseEntity<Actor> responseOnUpdated = testRestTemplate.withBasicAuth("user", "password")
                .exchange("/api/v1/actors/update/" + actor.getId(), HttpMethod.PUT, httpEntity, Actor.class);

        assertThat(responseOnUpdated.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToUpdateActor() {
        Actor actor = new Actor("Test", "Test", "Test");

        // Create actor as admin to be sure
        ResponseEntity<Actor> response = testRestTemplate.
                withBasicAuth("admin", "password").
                postForEntity("/api/v1/actors/create", actor, Actor.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // We don't have to change actor - just test permissions
        actor = response.getBody();
        HttpEntity<Actor> httpEntity = new HttpEntity<>(actor, new HttpHeaders());
        ResponseEntity<Actor> responseOnUpdated = testRestTemplate.withBasicAuth("admin", "password")
                .exchange("/api/v1/actors/update/" + actor.getId(), HttpMethod.PUT, httpEntity, Actor.class);

        assertThat(responseOnUpdated.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // DirectorController - get all directors
    @Test
    public void ensureThatGuestIsAbleToReceiveAllDirectorsList() {
        ResponseEntity<String> response = testRestTemplate.
                getForEntity("/api/v1/directors/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatUserIsAbleToReceiveAllDirectorsList() {
        ResponseEntity<String> response = testRestTemplate.
                withBasicAuth("user", "password").
                getForEntity("/api/v1/directors/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToReceiveAllDirectorsList() {
        ResponseEntity<String> response = testRestTemplate.
                withBasicAuth("admin", "password").
                getForEntity("/api/v1/directors/all", String.class);

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
        ResponseEntity<String> response = testRestTemplate.
                withBasicAuth("user", "password").
                getForEntity("/api/v1/directors/name?name=some", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToReceiveDirectorsListByName() {
        ResponseEntity<String> response = testRestTemplate.
                withBasicAuth("admin", "password").
                getForEntity("/api/v1/directors/name?name=some", String.class);

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
        ResponseEntity<String> response = testRestTemplate.
                withBasicAuth("user", "password").
                getForEntity("/api/v1/directors/surname?surname=some", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToReceiveDirectorsListBySurname() {
        ResponseEntity<String> response = testRestTemplate.
                withBasicAuth("admin", "password").
                getForEntity("/api/v1/directors/surname?surname=some", String.class);

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
        ResponseEntity<String> response = testRestTemplate.
                withBasicAuth("user", "password").
                getForEntity("/api/v1/directors/nameorsurname?name=some&surname=some", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToReceiveDirectorsListByNameOrSurname() {
        ResponseEntity<String> response = testRestTemplate.
                withBasicAuth("admin", "password").
                getForEntity("/api/v1/directors/nameorsurname?name=some&surname=some", String.class);

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

        ResponseEntity<Director> response = testRestTemplate.
                withBasicAuth("user", "password").
                postForEntity("/api/v1/directors/create", director, Director.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToCreateNewDirector() {
        Director director = new Director("Test", "test", "test");

        ResponseEntity<Director> response = testRestTemplate.
                withBasicAuth("admin", "password").
                postForEntity("/api/v1/directors/create", director, Director.class);

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
        ResponseEntity<Void> response = testRestTemplate.withBasicAuth("user", "password")
                .exchange("/api/v1/directors/delete?id=1", HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToDeleteDirector() {
        Director director = new Director("Test", "test", "test");

        // Create director as admin to be sure
        ResponseEntity<Director> response = testRestTemplate.
                withBasicAuth("admin", "password").
                postForEntity("/api/v1/directors/create", director, Director.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        director = response.getBody();
        ResponseEntity<Void> responseOnDelete = testRestTemplate.withBasicAuth("admin", "password")
                .exchange("/api/v1/directors/delete?id=" + director.getId(), HttpMethod.DELETE, null, Void.class);

        assertThat(responseOnDelete.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // DirectorController - update existing director
    @Test
    public void ensureThatGuestIsNotAbleToUpdateDirector() {
        Director director = new Director("Test", "test", "test");

        // Create director as admin to be sure
        ResponseEntity<Director> response = testRestTemplate.
                withBasicAuth("admin", "password").
                postForEntity("/api/v1/directors/create", director, Director.class);

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
        ResponseEntity<Director> response = testRestTemplate.
                withBasicAuth("admin", "password").
                postForEntity("/api/v1/directors/create", director, Director.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // We don't have to change director - just test permissions
        director = response.getBody();
        HttpEntity<Director> httpEntity = new HttpEntity<>(director, new HttpHeaders());
        ResponseEntity<Director> responseOnUpdated = testRestTemplate.withBasicAuth("user", "password")
                .exchange("/api/v1/directors/update/" + director.getId(), HttpMethod.PUT, httpEntity, Director.class);

        assertThat(responseOnUpdated.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToUpdateDirector() {
        Director director = new Director("Test", "test", "test");

        // Create director as admin to be sure
        ResponseEntity<Director> response = testRestTemplate.
                withBasicAuth("admin", "password").
                postForEntity("/api/v1/directors/create", director, Director.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // We don't have to change director - just test permissions
        director = response.getBody();
        HttpEntity<Director> httpEntity = new HttpEntity<>(director, new HttpHeaders());
        ResponseEntity<Director> responseOnUpdated = testRestTemplate.withBasicAuth("admin", "password")
                .exchange("/api/v1/directors/update/" + director.getId(), HttpMethod.PUT, httpEntity, Director.class);

        assertThat(responseOnUpdated.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
