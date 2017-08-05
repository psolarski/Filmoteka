package pl.filmoteka.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import pl.filmoteka.AuthorizedTestsBase;
import pl.filmoteka.model.Actor;
import pl.filmoteka.model.Movie;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSecurityActorsControllerTests extends AuthorizedTestsBase {

    // ActorController - get all actors
    @Test
    public void ensureThatGuestIsAbleToReceiveAllActorsList() {
        ResponseEntity<String> allActorsResponse = testRestTemplate.
                getForEntity("/api/v1/actors/all", String.class);

        assertThat(allActorsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatUserIsAbleToReceiveAllActorsList() {
        ResponseEntity<String> allActorsResponse = testRestTemplateAsUser
                .getForEntity("/api/v1/actors/all", String.class);

        assertThat(allActorsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToReceiveAllActorsList() {
        ResponseEntity<String> allActorsResponse = testRestTemplateAsAdmin
                .getForEntity("/api/v1/actors/all", String.class);

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
        ResponseEntity<String> allActorsResponse = testRestTemplateAsUser
                .getForEntity("/api/v1/actors/name?name=some", String.class);

        assertThat(allActorsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToReceiveActorsListByName() {
        ResponseEntity<String> allActorsResponse = testRestTemplateAsAdmin
                .getForEntity("/api/v1/actors/name?name=some", String.class);

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
        ResponseEntity<String> allActorsResponse = testRestTemplateAsUser
                .getForEntity("/api/v1/actors/surname?surname=some", String.class);

        assertThat(allActorsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToReceiveActorsListBySurname() {
        ResponseEntity<String> allActorsResponse = testRestTemplateAsAdmin
                .getForEntity("/api/v1/actors/surname?surname=some", String.class);

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
        ResponseEntity<String> allActorsResponse = testRestTemplateAsUser
                .getForEntity("/api/v1/actors/nameorsurname?name=some&surname=some", String.class);

        assertThat(allActorsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToReceiveActorsListByNameOrSurname() {
        ResponseEntity<String> allActorsResponse = testRestTemplateAsAdmin
                .getForEntity("/api/v1/actors/nameorsurname?name=some&surname=some", String.class);

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

        ResponseEntity<Actor> newActorResponse = testRestTemplateAsUser
                .postForEntity("/api/v1/actors/create", actor, Actor.class);

        assertThat(newActorResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToCreateNewActor() {
        Actor actor = new Actor("Test", "test", "test");

        ResponseEntity<Actor> newActorResponse = testRestTemplateAsAdmin
                .postForEntity("/api/v1/actors/create", actor, Actor.class);

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
        ResponseEntity<Void> response = testRestTemplateAsUser
                .exchange("/api/v1/actors/delete?id=1", HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToDeleteActor() {
        ResponseEntity<Void> response = testRestTemplateAsAdmin
                .exchange("/api/v1/actors/delete?id=1", HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // ActorController - update existing actor
    @Test
    public void ensureThatGuestIsNotAbleToUpdateActor() {
        Actor actor = new Actor("Test", "Test", "Test");

        // Create actor as admin to be sure
        ResponseEntity<Actor> response = testRestTemplateAsAdmin
                .postForEntity("/api/v1/actors/create", actor, Actor.class);

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
        ResponseEntity<Actor> response = testRestTemplateAsAdmin
                .postForEntity("/api/v1/actors/create", actor, Actor.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // We don't have to change actor - just test permissions
        actor = response.getBody();
        HttpEntity<Actor> httpEntity = new HttpEntity<>(actor, new HttpHeaders());
        ResponseEntity<Actor> responseOnUpdated = testRestTemplateAsUser
                .exchange("/api/v1/actors/update/" + actor.getId(), HttpMethod.PUT, httpEntity, Actor.class);

        assertThat(responseOnUpdated.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToUpdateActor() {
        Actor actor = new Actor("Test", "Test", "Test");

        // Create actor as admin to be sure
        ResponseEntity<Actor> response = testRestTemplateAsAdmin
                .postForEntity("/api/v1/actors/create", actor, Actor.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // We don't have to change actor - just test permissions
        actor = response.getBody();
        HttpEntity<Actor> httpEntity = new HttpEntity<>(actor, new HttpHeaders());
        ResponseEntity<Actor> responseOnUpdated = testRestTemplateAsAdmin
                .exchange("/api/v1/actors/update/" + actor.getId(), HttpMethod.PUT, httpEntity, Actor.class);

        assertThat(responseOnUpdated.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // ActorController - assign actor to chosen movie
    @Test
    public void ensureThatGuestIsNotAbleToAssignMovieToActor() {
        Movie someMovie = testRestTemplate.getForEntity("/api/v1/movies/all", Movie[].class).getBody()[0];
        ResponseEntity<Void> response = testRestTemplate.postForEntity("/api/v1/actors/movie?id=1", someMovie, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsNotAbleToAssignMovieToActor() {
        Movie someMovie = testRestTemplate.getForEntity("/api/v1/movies/all", Movie[].class).getBody()[0];
        ResponseEntity<Void> response = testRestTemplateAsUser
                .postForEntity("/api/v1/actors/movie?id=1", someMovie, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToAssignMovieToActor() {
        Actor someActor = new Actor("assignPuppet", "nosurname", "nationality");
        someActor = testRestTemplateAsAdmin
                .postForEntity("/api/v1/actors/create", someActor, Actor.class).getBody();
        Movie someMovie = testRestTemplate.getForEntity("/api/v1/movies/all", Movie[].class).getBody()[0];
        ResponseEntity<Void> response = testRestTemplateAsAdmin
                .postForEntity("/api/v1/actors/movie?id=" + someActor.getId(), someMovie, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
