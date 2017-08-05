package pl.filmoteka.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import pl.filmoteka.AuthorizedTestsBase;
import pl.filmoteka.model.Role;
import pl.filmoteka.model.SimilarityDegree;
import pl.filmoteka.model.User;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSecurityUsersControllerTests extends AuthorizedTestsBase {

    // UsersController - find all users
    @Test
    public void ensureThatGuestIsNotAbleToFindAllUsers() {
        ResponseEntity<User[]> response = testRestTemplate.getForEntity("/api/v1/users/all", User[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsAbleToFindAllUsers() {
        ResponseEntity<User[]> response = testRestTemplateAsUser.getForEntity("/api/v1/users/all", User[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToFindAllUsers() {
        ResponseEntity<User[]> response = testRestTemplateAsAdmin.getForEntity("/api/v1/users/all", User[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // UsersController - find all users by username
    @Test
    public void ensureThatGuestIsNotAbleToFindAllUsersByUsername() {
        ResponseEntity<User[]> response = testRestTemplate.getForEntity(
                "/api/v1/users/username?username=no",
                User[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsAbleToFindAllUsersByUsername() {
        ResponseEntity<User[]> response = testRestTemplateAsUser
                .getForEntity("/api/v1/users/username?username=no", User[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToFindAllUsersByUsername() {
        ResponseEntity<User[]> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/users/username?username=no", User[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // UsersController - find all users by email
    @Test
    public void ensureThatGuestIsNotAbleToFindAllUsersByEmail() {
        ResponseEntity<User[]> response = testRestTemplate.getForEntity(
                "/api/v1/users/email?email=no",
                User[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsAbleToFindAllUsersByEmail() {
        ResponseEntity<User[]> response = testRestTemplateAsUser
                .getForEntity("/api/v1/users/email?email=no", User[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToFindAllUsersByEmail() {
        ResponseEntity<User[]> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/users/email?email=no", User[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // UsersController - create user
    @Test
    public void ensureThatGuestIsAbleToCreateUser() {
        User user = new User("createUserTestGuest", "pass", "email@mail.12czx");
        ResponseEntity<User> response = testRestTemplate.postForEntity("/api/v1/users/create", user, User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatUserIsAbleToCreateUser() {
        User user = new User("createUserTestUser", "pass", "email@mail.12czx");
        ResponseEntity<User> response = testRestTemplateAsUser
                .postForEntity("/api/v1/users/create", user, User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToCreateUser() {
        User user = new User("createUserTestAdmin", "pass", "email@mail.12czx");
        ResponseEntity<User> response = testRestTemplateAsAdmin
                .postForEntity("/api/v1/users/create", user, User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // UsersController - delete user
    @Test
    public void ensureThatGuestIsNotAbleToDeleteUser() {
        ResponseEntity<Void> response = testRestTemplate
                .exchange("/api/v1/users/delete?id=0", HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsNotAbleToDeleteUser() {
        ResponseEntity<Void> response = testRestTemplateAsUser
                .exchange("/api/v1/users/delete?id=0", HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToDeleteUser() {
        User user = new User("deleteUserTestAdmin", "pass", "email@mail.12czx");
        user = testRestTemplateAsAdmin
                .postForEntity("/api/v1/users/create", user, User.class).getBody();

        ResponseEntity<Void> response = testRestTemplateAsAdmin
                .exchange("/api/v1/users/delete?id=" + user.getId(), HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // UsersController - update user
    @Test
    public void ensureThatGuestIsNotAbleToUpdateUser() {
        ResponseEntity<User> response = testRestTemplate
                .exchange("/api/v1/users/update/0", HttpMethod.PUT, null, User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsAbleToUpdateUser() {
        User user = testRestTemplateAsAdmin.getForEntity(
                "/api/v1/users/username?username=username7",
                User.class).getBody();
        HttpEntity<User> httpEntity = new HttpEntity<>(user, new HttpHeaders());
        ResponseEntity<User> response = testRestTemplateAsUser
                .exchange("/api/v1/users/update/" + user.getId(), HttpMethod.PUT, httpEntity, User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsAbleToUpdateUser() {
        User user = testRestTemplateAsAdmin.getForEntity(
                "/api/v1/users/username?username=" + adminUserUsername,
                User.class).getBody();
        HttpEntity<User> httpEntity = new HttpEntity<>(user, new HttpHeaders());
        ResponseEntity<User> response = testRestTemplateAsAdmin
                .exchange("/api/v1/users/update/" + user.getId(), HttpMethod.PUT, httpEntity, User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // UsersController - assign role
    @Test
    public void ensureThatGuestIsNotAbleToAssignRole() {
        Role role = new Role("USER");
        ResponseEntity<User> response = testRestTemplate.postForEntity(
                "/api/v1/users/1/assignrole", role, User.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsNotAbleToAssignRole() {
        Role role = new Role("USER");
        ResponseEntity<User> response = testRestTemplateAsUser
                .postForEntity("/api/v1/users/1/assignrole", role, User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToAssignRole() {
        Role role = new Role("USER");
        ResponseEntity<User> response = testRestTemplateAsAdmin
                .postForEntity("/api/v1/users/2/assignrole", role, User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // UsersController - check similarity
    @Test
    public void ensureThatGuestIsNotAbleToGetSimilarity() {
        ResponseEntity<SimilarityDegree> response = testRestTemplate.getForEntity(
                "/api/v1/users/similarity/1", SimilarityDegree.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsAbleToGetSimilarity() {
        ResponseEntity<SimilarityDegree> response = testRestTemplateAsUser.getForEntity(
                "/api/v1/users/similarity/1", SimilarityDegree.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void ensureThatAdminIsNotAbleToGetSimilarity() {
        ResponseEntity<SimilarityDegree> response = testRestTemplateAsAdmin.getForEntity(
                "/api/v1/users/similarity/1", SimilarityDegree.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
