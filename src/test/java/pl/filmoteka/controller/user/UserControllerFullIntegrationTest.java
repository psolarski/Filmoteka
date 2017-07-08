package pl.filmoteka.controller.user;

import com.jayway.restassured.path.json.JsonPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.filmoteka.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for UserController.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ComponentScan("pl.filmoteka")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerFullIntegrationTest {

    @Value("${test.db.initializer.users.size}")
    private Integer usersSize;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void getAllUsers() {
        ResponseEntity<String> response = testRestTemplate.withBasicAuth("user", "password")
                .getForEntity("/api/v1/users/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        assertThat((List<?>) path.get("login")).isNotNull().isNotEmpty().hasSize(usersSize);
        assertThat((List<?>) path.get("password")).isNotNull().isNotEmpty().hasSize(usersSize);
        assertThat((List<?>) path.get("email")).isNotNull().isNotEmpty().hasSize(usersSize);
    }

    @Test
    public void getUsersByLogin() {
        ResponseEntity<String> response = testRestTemplate.withBasicAuth("user", "password")
                .getForEntity("/api/v1/users/login?login=login2", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        assertThat((List<?>) path.get("login")).isNotNull().isNotEmpty()
                .hasSize(1);
    }

    @Test
    public void getUsersByEmail() {
        ResponseEntity<String> response = testRestTemplate.withBasicAuth("user", "password")
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

        ResponseEntity<String> response = testRestTemplate.withBasicAuth("user", "password")
                .getForEntity("/api/v1/users/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        List <String> returnedLogins = path.get("login");
        assertThat(returnedLogins).isNotNull().isNotEmpty().doesNotContain("login0");
    }
}
