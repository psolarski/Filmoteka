package pl.filmoteka.controller.user;

import com.jayway.restassured.path.json.JsonPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import pl.filmoteka.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-end test for UserController.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerFullIntegrationTest {

    @Value("${test.db.initializer.users.size}")
    private Integer usersSize;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void getAllUsers() {
        ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity("/api/v1/users/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        assertThat((List<?>) path.get("username")).isNotNull().isNotEmpty();
        assertThat((List<?>) path.get("password")).isNotNull().isNotEmpty();
        assertThat((List<?>) path.get("email")).isNotNull().isNotEmpty();
    }

    @Test
    public void getUsersByUsername() {
        ResponseEntity<User> response = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity("/api/v1/users/username?username=username2", User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(response).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo("username2");
    }

    @Test
    public void getUsersByEmail() {
        ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity("/api/v1/users/email?email=doo2@bee.doo", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        assertThat((List<?>) path.get("email")).isNotNull().isNotEmpty()
                .hasSize(1);
    }

    @Test
    public void createUser() {
        User user = new User("createUserTest", "password", "createUserTest@foo.bar");
        ResponseEntity<User> response = testRestTemplate.withBasicAuth("admin", "password")
                .postForEntity("/api/v1/users/create", user, User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(user.equals(response.getBody()));
    }

    @Test
    public void deleteUser() {
        testRestTemplate.withBasicAuth("admin", "password").delete("/api/v1/users/delete?id=1");

        ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity("/api/v1/users/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        List<String> returnedLogins = path.get("username");
        assertThat(returnedLogins).isNotNull().isNotEmpty().doesNotContain("username0");
    }

    @Test
    public void updateUser() {
        // First, create an User
        User user = new User("updateUserTest", "password", "updateUserTest@email.com");

        ResponseEntity<User> responseOnCreated = testRestTemplate.withBasicAuth("admin", "password")
                .postForEntity("/api/v1/users/create", user, User.class);
        assertThat(responseOnCreated.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Check whether the application properly stored the User
        ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity("/api/v1/users/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        List<String> returnedNames = path.get("username");
        assertThat(returnedNames).isNotNull().isNotEmpty().contains("updateUserTest");

        // Update User
        user = responseOnCreated.getBody();
        user.setEmail("updateDirectorTestNewEmail@email.com");

        HttpEntity<User> httpEntity = new HttpEntity<>(user, new HttpHeaders());
        ResponseEntity<User> responseOnUpdated = testRestTemplate.withBasicAuth("admin", "password")
                .exchange("/api/v1/users/update/" + user.getId(), HttpMethod.PUT, httpEntity, User.class);
        assertThat(responseOnUpdated.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Get list of all directors to be sure
        ResponseEntity<String> responseAfterUpdate = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity("/api/v1/users/all", String.class);

        assertThat(responseAfterUpdate.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath pathAfterUpdate = new JsonPath(responseAfterUpdate.getBody());

        List<String> returnedEmailsAfterUpdate = pathAfterUpdate.get("email");
        assertThat(returnedEmailsAfterUpdate).isNotNull().isNotEmpty().contains("updateDirectorTestNewEmail@email.com");
    }
}
