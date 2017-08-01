package pl.filmoteka.security;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import pl.filmoteka.model.Actor;
import pl.filmoteka.model.Director;
import pl.filmoteka.model.Movie;
import pl.filmoteka.model.Role;
import pl.filmoteka.repository.DirectorRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSecurityActorControllerTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private DirectorRepository directorRepository;

    private Director director;

    @Before
    public void init() {
        if (director == null) {
            director = new Director("MovieRepositoryTestDirector", "surname", "nationality");
            director = directorRepository.saveAndFlush(director);
        }
    }

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

    // MovieController - get all movie
    @Test
    public void ensureThatGuestIsAbleToReceiveAllMoviesList() {
        ResponseEntity<String> response = testRestTemplate.
                getForEntity("/api/v1/movies/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatUserIsAbleToReceiveAllMoviesList() {
        ResponseEntity<String> response = testRestTemplate.
                withBasicAuth("user", "password").
                getForEntity("/api/v1/movies/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToReceiveAllMoviesList() {
        ResponseEntity<String> response = testRestTemplate.
                withBasicAuth("admin", "password").
                getForEntity("/api/v1/movies/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // MovieController - get movies by name
    @Test
    public void ensureThatGuestIsAbleToReceiveMoviesListByName() {
        ResponseEntity<String> response = testRestTemplate.
                getForEntity("/api/v1/movies/name?name=some", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatUserIsAbleToReceiveMoviesListByName() {
        ResponseEntity<String> response = testRestTemplate.
                withBasicAuth("user", "password").
                getForEntity("/api/v1/movies/name?name=some", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToReceiveMoviesListByName() {
        ResponseEntity<String> response = testRestTemplate.
                withBasicAuth("admin", "password").
                getForEntity("/api/v1/movies/name?name=some", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // MovieController - get directors by surname
    @Test
    public void ensureThatGuestIsAbleToReceiveMoviesListByGenre() {
        ResponseEntity<String> response = testRestTemplate.
                getForEntity("/api/v1/movies/genre?genre=some", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatUserIsAbleToReceiveMoviesListBySurname() {
        ResponseEntity<String> response = testRestTemplate.
                withBasicAuth("user", "password").
                getForEntity("/api/v1/movies/genre?genre=some", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToReceiveMoviesListByGenre() {
        ResponseEntity<String> response = testRestTemplate.
                withBasicAuth("admin", "password").
                getForEntity("/api/v1/movies/genre?genre=some", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // MovieController - create movie
    @Test
    public void ensureThatGuestIsNotAbleToCreateMovie() {
        Movie movie = new Movie("movieName", 110, "Action", LocalDate.now(), "English");
        movie.setDirector(director);

        ResponseEntity<Movie> response = testRestTemplate.
                postForEntity("/api/v1/movies/create", movie, Movie.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsNotAbleToCreateMovie() {
        Movie movie = new Movie("movieName", 110, "Action", LocalDate.now(), "English");
        movie.setDirector(director);

        ResponseEntity<Movie> response = testRestTemplate.
                withBasicAuth("user", "password").
                postForEntity("/api/v1/movies/create", movie, Movie.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToCreateNewMovie() {
        Movie movie = new Movie("movieName", 110, "Action", LocalDate.now(), "English");
        movie.setDirector(director);

        ResponseEntity<Movie> response = testRestTemplate.
                withBasicAuth("admin", "password").
                postForEntity("/api/v1/movies/create", movie, Movie.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // MovieController - delete movie
    // We can just try to delete some nonexistent movie
    @Test
    public void ensureThatGuestIsNotAbleToDeleteMovie() {
        ResponseEntity<Void> response = testRestTemplate
                .exchange("/api/v1/movies/delete?id=1", HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsNotAbleToDeleteMovie() {
        ResponseEntity<Void> response = testRestTemplate.withBasicAuth("user", "password")
                .exchange("/api/v1/movies/delete?id=1", HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToDeleteMovie() {
        Movie movie = new Movie("movieName", 110, "Action", LocalDate.now(), "English");
        movie.setDirector(director);

        ResponseEntity<Movie> response = testRestTemplate.
                withBasicAuth("admin", "password").
                postForEntity("/api/v1/movies/create", movie, Movie.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        movie = response.getBody();
        ResponseEntity<Void> responseOnDelete = testRestTemplate.withBasicAuth("admin", "password")
                .exchange("/api/v1/movies/delete?id=" + movie.getId(), HttpMethod.DELETE, null, Void.class);

        assertThat(responseOnDelete.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // MovieController - update existing director
    @Test
    public void ensureThatGuestIsNotAbleToUpdateMovie() {
        Movie movie = new Movie("movieName", 110, "Action", LocalDate.now(), "English");
        movie.setDirector(director);

        ResponseEntity<Movie> response = testRestTemplate.
                withBasicAuth("admin", "password").
                postForEntity("/api/v1/movies/create", movie, Movie.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // We don't have to change movie - just test permissions
        movie = response.getBody();
        HttpEntity<Movie> httpEntity = new HttpEntity<>(movie, new HttpHeaders());
        ResponseEntity<Director> responseOnUpdated = testRestTemplate
                .exchange("/api/v1/movies/update/" + movie.getId(), HttpMethod.PUT, httpEntity, Director.class);

        assertThat(responseOnUpdated.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsNotAbleToUpdateMovie() {
        Movie movie = new Movie("movieName", 110, "Action", LocalDate.now(), "English");
        movie.setDirector(director);

        ResponseEntity<Movie> response = testRestTemplate.
                withBasicAuth("admin", "password").
                postForEntity("/api/v1/movies/create", movie, Movie.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // We don't have to change movie - just test permissions
        movie = response.getBody();
        HttpEntity<Movie> httpEntity = new HttpEntity<>(movie, new HttpHeaders());
        ResponseEntity<Director> responseOnUpdated = testRestTemplate.withBasicAuth("user", "password")
                .exchange("/api/v1/movies/update/" + movie.getId(), HttpMethod.PUT, httpEntity, Director.class);

        assertThat(responseOnUpdated.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToUpdateMovie() {
        Movie movie = new Movie("movieName", 110, "Action", LocalDate.now(), "English");
        movie.setDirector(director);

        ResponseEntity<Movie> response = testRestTemplate.
                withBasicAuth("admin", "password").
                postForEntity("/api/v1/movies/create", movie, Movie.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // We don't have to change movie - just test permissions
        movie = response.getBody();
        HttpEntity<Movie> httpEntity = new HttpEntity<>(movie, new HttpHeaders());
        ResponseEntity<Director> responseOnUpdated = testRestTemplate.withBasicAuth("admin", "password")
                .exchange("/api/v1/movies/update/" + movie.getId(), HttpMethod.PUT, httpEntity, Director.class);

        assertThat(responseOnUpdated.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // RolesController - get all roles
    @Test
    public void ensureThatGuestIsNotAbleToGetAllRoles() {
        ResponseEntity<String> response = testRestTemplate.getForEntity("/api/v1/roles/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsNotAbleToGetAllRoles() {
        ResponseEntity<String> response = testRestTemplate.
                withBasicAuth("user", "password").getForEntity("/api/v1/roles/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToGetAllRoles() {
        ResponseEntity<String> response = testRestTemplate.
                withBasicAuth("admin", "password").getForEntity("/api/v1/roles/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // RolesController - get role USER by its name
    @Test
    public void ensureThatGuestIsNotAbleToGetSingleRoleByName() {
        ResponseEntity<String> response = testRestTemplate
                .getForEntity("/api/v1/roles/name/USER", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsNotAbleToGetSingleRoleByName() {
        ResponseEntity<String> response = testRestTemplate.
                withBasicAuth("user", "password").getForEntity("/api/v1/roles/name/USER", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToGetSingleRoleByName() {
        ResponseEntity<String> response = testRestTemplate.
                withBasicAuth("admin", "password").getForEntity("/api/v1/roles/name/USER", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // RolesController - create new role
    @Test
    public void ensureThatGuestIsNotAbleToCreateRole() {
        Role role = new Role("TESTGUESTROLE");
        ResponseEntity<Role> response = testRestTemplate
                .postForEntity("/api/v1/roles/create", role, Role.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsNotAbleToCreateRole() {
        Role role = new Role("TESTUSERROLE");
        ResponseEntity<Role> response = testRestTemplate.
                withBasicAuth("user", "password").postForEntity("/api/v1/roles/create", role, Role.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToCreateRole() {
        Role role = new Role("TESTADMINROLE");
        ResponseEntity<Role> response = testRestTemplate.
                withBasicAuth("admin", "password").postForEntity("/api/v1/roles/create", role, Role.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
