package pl.filmoteka.controller.user;

import com.jayway.restassured.path.json.JsonPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import pl.filmoteka.AuthorizedTestsBase;
import pl.filmoteka.model.Role;
import pl.filmoteka.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-end test for UserController.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerFullIntegrationTest extends AuthorizedTestsBase {

    @Value("${test.db.initializer.users.size}")
    private Integer usersSize;

    @Test
    public void getAllUsers() {
        ResponseEntity<String> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/users/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        assertThat((List<?>) path.get("username")).isNotNull().isNotEmpty();
        assertThat((List<?>) path.get("password")).isNotNull().isNotEmpty();
        assertThat((List<?>) path.get("email")).isNotNull().isNotEmpty();
    }

    @Test
    public void getUsersByUsername() {
        ResponseEntity<User> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/users/username?username=username2", User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(response).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo("username2");
    }

    @Test
    public void getUsersByEmail() {
        ResponseEntity<String> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/users/email?email=doo2@bee.doo", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        assertThat((List<?>) path.get("email")).isNotNull().isNotEmpty()
                .hasSize(1);
    }

    @Test
    public void createUser() {
        User user = new User("createUserTest", "password", "createUserTest@foo.bar");
        ResponseEntity<User> response = testRestTemplateAsAdmin
                .postForEntity("/api/v1/users/create", user, User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(user.equals(response.getBody()));
    }

    @Test
    public void deleteUser() {
        testRestTemplateAsAdmin.delete("/api/v1/users/delete?id=3");

        ResponseEntity<String> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/users/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        List<String> returnedLogins = path.get("username");
        assertThat(returnedLogins).isNotNull().isNotEmpty().doesNotContain("username0");
    }

    @Test
    public void updateUser() {
        // First, create an user
        User user = new User("updateUserTest", "password", "updateUserTest@email.com");

        ResponseEntity<User> responseOnCreated = testRestTemplateAsAdmin
                .postForEntity("/api/v1/users/create", user, User.class);
        assertThat(responseOnCreated.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Check whether the application properly stored the user
        ResponseEntity<String> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/users/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        List<String> returnedNames = path.get("username");
        assertThat(returnedNames).isNotNull().isNotEmpty().contains("updateUserTest");

        // Update user
        user = responseOnCreated.getBody();
        user.setEmail("updateDirectorTestNewEmail@email.com");

        HttpEntity<User> httpEntity = new HttpEntity<>(user, new HttpHeaders());
        ResponseEntity<User> responseOnUpdated = testRestTemplateAsAdmin
                .exchange("/api/v1/users/update/" + user.getId(), HttpMethod.PUT, httpEntity, User.class);
        assertThat(responseOnUpdated.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Get list of all directors to be sure
        ResponseEntity<String> responseAfterUpdate = testRestTemplateAsAdmin
                .getForEntity("/api/v1/users/all", String.class);

        assertThat(responseAfterUpdate.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath pathAfterUpdate = new JsonPath(responseAfterUpdate.getBody());

        List<String> returnedEmailsAfterUpdate = pathAfterUpdate.get("email");
        assertThat(returnedEmailsAfterUpdate).isNotNull().isNotEmpty().contains("updateDirectorTestNewEmail@email.com");
    }

    @Test
    public void testAssignRoleToUser() {
        User user = new User("testAssignRole", "password", "testAssignRole@email.com");

        ResponseEntity<User> responseOnCreated = testRestTemplateAsAdmin
                .postForEntity("/api/v1/users/create", user, User.class);
        assertThat(responseOnCreated.getStatusCode()).isEqualTo(HttpStatus.OK);

        user = responseOnCreated.getBody();

        assertThat(user.getRoles()).isEmpty();

        Role role = testRestTemplateAsAdmin.getForEntity("/api/v1/roles/name/USER", Role.class).getBody();
        ResponseEntity<User> responseOnModified = testRestTemplateAsAdmin
                .postForEntity("/api/v1/users/" + user.getId() + "/assignrole", role, User.class);
        user = responseOnModified.getBody();

        assertThat(responseOnModified.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(user.getRoles().stream().anyMatch(r -> r.getName().equals("USER"))).isTrue();
    }
}
