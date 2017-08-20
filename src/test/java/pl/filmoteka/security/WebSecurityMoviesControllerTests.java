package pl.filmoteka.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import pl.filmoteka.AuthorizedTestsBase;
import pl.filmoteka.model.Director;
import pl.filmoteka.model.Movie;
import pl.filmoteka.model.User;
import pl.filmoteka.model.integration.MusicAlbum;
import pl.filmoteka.model.integration.NytCriticReview;
import pl.filmoteka.model.integration.ProductList;
import pl.filmoteka.repository.DirectorRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSecurityMoviesControllerTests extends AuthorizedTestsBase {

    @Autowired
    private DirectorRepository directorRepository;

    private Director director;

    @Before
    public void init() {
        if (director == null) {
            director = new Director("wsMovieControllerTestDirector", "surname", "nationality");
            director = directorRepository.saveAndFlush(director);
        }
    }

    // MovieController - get all movies
    @Test
    public void ensureThatGuestIsAbleToReceiveAllMoviesList() {
        ResponseEntity<String> response = testRestTemplate.
                getForEntity("/api/v1/movies/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatUserIsAbleToReceiveAllMoviesList() {
        ResponseEntity<String> response = testRestTemplateAsUser
                .getForEntity("/api/v1/movies/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToReceiveAllMoviesList() {
        System.out.println(testRestTemplateAsAdmin == null ? "yes" : "no");
        ResponseEntity<String> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/movies/all", String.class);

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
        ResponseEntity<String> response = testRestTemplateAsUser
                .getForEntity("/api/v1/movies/name?name=some", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToReceiveMoviesListByName() {
        ResponseEntity<String> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/movies/name?name=some", String.class);

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
    public void ensureThatUserIsAbleToReceiveMoviesListByGenre() {
        ResponseEntity<String> response = testRestTemplateAsUser
                .getForEntity("/api/v1/movies/genre?genre=some", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToReceiveMoviesListByGenre() {
        ResponseEntity<String> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/movies/genre?genre=some", String.class);

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

        ResponseEntity<Movie> response = testRestTemplateAsUser
                .postForEntity("/api/v1/movies/create", movie, Movie.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToCreateNewMovie() {
        Movie movie = new Movie("movieName", 110, "Action", LocalDate.now(), "English");
        movie.setDirector(director);

        ResponseEntity<Movie> response = testRestTemplateAsAdmin
                .postForEntity("/api/v1/movies/create", movie, Movie.class);

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
        ResponseEntity<Void> response = testRestTemplateAsUser
                .exchange("/api/v1/movies/delete?id=1", HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToDeleteMovie() {
        Movie movie = new Movie("movieName", 110, "Action", LocalDate.now(), "English");
        movie.setDirector(director);

        ResponseEntity<Movie> response = testRestTemplateAsAdmin
                .postForEntity("/api/v1/movies/create", movie, Movie.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        movie = response.getBody();
        ResponseEntity<Void> responseOnDelete = testRestTemplateAsAdmin
                .exchange("/api/v1/movies/delete?id=" + movie.getId(), HttpMethod.DELETE, null, Void.class);

        assertThat(responseOnDelete.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // MovieController - update existing movie
    @Test
    public void ensureThatGuestIsNotAbleToUpdateMovie() {
        Movie movie = new Movie("movieName", 110, "Action", LocalDate.now(), "English");
        movie.setDirector(director);

        ResponseEntity<Movie> response = testRestTemplateAsAdmin
                .postForEntity("/api/v1/movies/create", movie, Movie.class);

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

        ResponseEntity<Movie> response = testRestTemplateAsAdmin
                .postForEntity("/api/v1/movies/create", movie, Movie.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // We don't have to change movie - just test permissions
        movie = response.getBody();
        HttpEntity<Movie> httpEntity = new HttpEntity<>(movie, new HttpHeaders());
        ResponseEntity<Director> responseOnUpdated = testRestTemplateAsUser
                .exchange("/api/v1/movies/update/" + movie.getId(), HttpMethod.PUT, httpEntity, Director.class);

        assertThat(responseOnUpdated.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToUpdateMovie() {
        Movie movie = new Movie("movieName", 110, "Action", LocalDate.now(), "English");
        movie.setDirector(director);

        ResponseEntity<Movie> response = testRestTemplateAsAdmin
                .postForEntity("/api/v1/movies/create", movie, Movie.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // We don't have to change movie - just test permissions
        movie = response.getBody();
        HttpEntity<Movie> httpEntity = new HttpEntity<>(movie, new HttpHeaders());
        ResponseEntity<Director> responseOnUpdated = testRestTemplateAsAdmin
                .exchange("/api/v1/movies/update/" + movie.getId(), HttpMethod.PUT, httpEntity, Director.class);

        assertThat(responseOnUpdated.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // MoviesController - get reviews for movie
    @Test
    public void ensureThatGuestIsAbleToGetMovieReviews() {
        ResponseEntity<NytCriticReview[]> response = testRestTemplate.getForEntity(
                "/api/v1/movies/4/reviews",
                NytCriticReview[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatUserIsAbleToGetMovieReviews() {
        ResponseEntity<NytCriticReview[]> response = testRestTemplateAsUser
                .getForEntity("/api/v1/movies/4/reviews", NytCriticReview[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToGetMovieReviews() {
        ResponseEntity<NytCriticReview[]> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/movies/4/reviews", NytCriticReview[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // MoviesController - get products for movie
    @Test
    public void ensureThatGuestIsAbleToGetMovieProducts() {
        ResponseEntity<ProductList[]> response = testRestTemplate.getForEntity(
                "/api/v1/movies/4/products",
                ProductList[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatUserIsAbleToGetMovieProducts() {
        ResponseEntity<ProductList[]> response = testRestTemplateAsUser
                .getForEntity("/api/v1/movies/4/products", ProductList[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToGetMovieProducts() {
        ResponseEntity<ProductList[]> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/movies/4/products", ProductList[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // MoviesController - get soundtrack for movie
    @Test
    public void ensureThatGuestIsAbleToGetMovieSoundtrack() {
        ResponseEntity<MusicAlbum[]> response = testRestTemplate.getForEntity(
                "/api/v1/movies/4/soundtrack",
                MusicAlbum[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatUserIsAbleToGetMovieSoundtrack() {
        ResponseEntity<MusicAlbum[]> response = testRestTemplateAsUser
                .getForEntity("/api/v1/movies/4/soundtrack", MusicAlbum[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToGetMovieSoundtrack() {
        ResponseEntity<MusicAlbum[]> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/movies/4/soundtrack", MusicAlbum[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // MoviesController - get n best rated movies
    @Test
    public void ensureThatGuestIsAbleToGet3BestMovies() {
        ResponseEntity<Movie[]> response = testRestTemplate.getForEntity("/api/v1/movies/rating/3", Movie[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatUserIsAbleToGet3BestMovies() {
        ResponseEntity<Movie[]> response = testRestTemplateAsUser
                .getForEntity("/api/v1/movies/rating/3", Movie[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToGet3BestMovies() {
        ResponseEntity<Movie[]> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/movies/rating/3", Movie[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // MoviesController - get movie recommendations
    @Test
    public void ensureThatGuestIsNotAbleToGetMovieRecommendations() {
        ResponseEntity<Movie[]> response = testRestTemplate.getForEntity("/api/v1/movies/recommendations", Movie[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsAbleToGetMovieRecommendations() {
        ResponseEntity<Movie[]> response = testRestTemplateAsUser
                .getForEntity("/api/v1/movies/recommendations", Movie[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToGetMovieRecommendations() {
        ResponseEntity<Movie[]> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/movies/recommendations", Movie[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    // MoviesController - add movie to watched list
    @Test
    public void ensureThatGuestIsNotAbleToSetMovieAsWatched() {
        ResponseEntity<User> response = testRestTemplate.getForEntity("/api/v1/movies/5/watched", User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsAbleToSetMovieAsWatched() {
        ResponseEntity<User> response = testRestTemplateAsUser
                .getForEntity("/api/v1/movies/5/watched", User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsNotAbleToSetMovieAsWatched() {
        ResponseEntity<User> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/movies/5/watched", User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    // MoviesController - assign actor to movie
    @Test
    public void ensureThatGuestIsNotAbleToAssignMovieToActor() {
        ResponseEntity<String> response = testRestTemplate.getForEntity(
                "/api/v1/movies/5/assign/actor/5",
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsNotAbleToAssignMovieToActor() {
        ResponseEntity<String> response = testRestTemplateAsUser
                .getForEntity("/api/v1/movies/5/assign/actor/5", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToAssignMovieToActor() {
        ResponseEntity<String> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/movies/5/assign/actor/5", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // MoviesController - assign director to movie
    @Test
    public void ensureThatGuestIsNotAbleToAssignMovieToDirector() {
        ResponseEntity<String> response = testRestTemplate.getForEntity(
                "/api/v1/movies/5/assign/director/5",
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsNotAbleToAssignMovieToDirector() {
        ResponseEntity<String> response = testRestTemplateAsUser
                .getForEntity("/api/v1/movies/5/assign/director/5", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToAssignMovieToDirector() {
        ResponseEntity<String> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/movies/5/assign/director/5", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToReceiveMovesFromTheMovieDB() {
        ResponseEntity response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/movies/movieFromMovieDb/Inception", Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
