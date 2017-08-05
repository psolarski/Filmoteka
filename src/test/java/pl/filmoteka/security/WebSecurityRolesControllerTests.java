package pl.filmoteka.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import pl.filmoteka.AuthorizedTestsBase;
import pl.filmoteka.model.Role;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSecurityRolesControllerTests extends AuthorizedTestsBase {

    // RolesController - get all roles
    @Test
    public void ensureThatGuestIsNotAbleToGetAllRoles() {
        ResponseEntity<String> response = testRestTemplate.getForEntity("/api/v1/roles/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void ensureThatUserIsNotAbleToGetAllRoles() {
        ResponseEntity<String> response = testRestTemplateAsUser.getForEntity("/api/v1/roles/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToGetAllRoles() {
        ResponseEntity<String> response = testRestTemplateAsAdmin.getForEntity("/api/v1/roles/all", String.class);

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
        ResponseEntity<String> response = testRestTemplateAsUser.getForEntity("/api/v1/roles/name/USER", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToGetSingleRoleByName() {
        ResponseEntity<String> response = testRestTemplateAsAdmin.getForEntity("/api/v1/roles/name/USER", String.class);

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
        ResponseEntity<Role> response = testRestTemplateAsUser.postForEntity("/api/v1/roles/create", role, Role.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void ensureThatAdminIsAbleToCreateRole() {
        Role role = new Role("TESTADMINROLE");
        ResponseEntity<Role> response = testRestTemplateAsAdmin.postForEntity("/api/v1/roles/create", role, Role.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
