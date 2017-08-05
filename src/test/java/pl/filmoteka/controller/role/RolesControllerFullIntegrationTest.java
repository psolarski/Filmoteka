package pl.filmoteka.controller.role;

import com.jayway.restassured.path.json.JsonPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import pl.filmoteka.AuthorizedTestsBase;
import pl.filmoteka.model.Role;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Marek on 7/20/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RolesControllerFullIntegrationTest extends AuthorizedTestsBase {

    @Test
    public void testGetAllRoles() {
        ResponseEntity<String> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/roles/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        assertThat((List<?>) path.get("name")).isNotNull().isNotEmpty();
    }

    @Test
    public void testGetUserRoleByName() {
        ResponseEntity<Role> response = testRestTemplateAsAdmin
                .getForEntity("/api/v1/roles/name/USER", Role.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isNotNull().isEqualTo("USER");
    }

    @Test
    public void testCreateNewRole() {
        Role role = new Role("TESTROLE");

        ResponseEntity<Role> response = testRestTemplateAsAdmin
                .postForEntity("/api/v1/roles/create", role, Role.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(role.equals(response.getBody()));
    }
}
