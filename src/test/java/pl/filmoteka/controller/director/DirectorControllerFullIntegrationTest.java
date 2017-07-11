package pl.filmoteka.controller.director;

import com.jayway.restassured.path.json.JsonPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import pl.filmoteka.model.Director;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-end test suite for director controller.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DirectorControllerFullIntegrationTest {

    @Value("${test.db.initializer.directors.size}")
    private Integer directorsSize;

    @Value("${test.db.initializer.movies.size}")
    private Integer moviesSize;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void getAllDirectors() {
        ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity("/api/v1/directors/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        List<Director> returnedDirectors = path.get();
        assertThat(returnedDirectors).isNotNull().isNotEmpty();
    }

    @Test
    public void getDirectorsByName() {
        ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity("/api/v1/directors/name?name=name7", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        assertThat((List<?>) path.get("name")).isNotNull().isNotEmpty()
                .hasSize(1);
    }

    @Test
    public void getDirectorsBySurname() {
        ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity("/api/v1/directors/surname?surname=surname7", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        assertThat((List<?>) path.get("surname")).isNotNull().isNotEmpty()
                .hasSize(1);
    }

    @Test
    public void getDirectorsByNameOrSurname() {
        ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity("/api/v1/directors/nameorsurname?name=name7&surname=surname8", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        assertThat((List<?>) path.get("name")).isNotNull().isNotEmpty()
                .hasSize(2);
    }

    @Test
    public void createDirector() {
        Director director = new Director("createDirectorTest", "someSurname", "American");

        ResponseEntity<Director> responseOnCreated = testRestTemplate.withBasicAuth("admin", "password")
                .postForEntity("/api/v1/directors/create", director, Director.class);
        assertThat(responseOnCreated.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(director.equals(responseOnCreated.getBody()));
    }

    @Test
    public void deleteDirector() {
        // First, create a director
        Director director = new Director("deleteDirectorTest", "someSurname", "American");

        ResponseEntity<Director> responseOnCreated = testRestTemplate.withBasicAuth("admin", "password")
                .postForEntity("/api/v1/directors/create", director, Director.class);
        assertThat(responseOnCreated.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Then delete it
        testRestTemplate.withBasicAuth("admin", "password")
                .delete("/api/v1/directors/delete?id=" + responseOnCreated.getBody().getId());

        ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity("/api/v1/directors/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        List <String> returnedNames = path.get("name");
        assertThat(returnedNames).isNotNull().isNotEmpty().doesNotContain("deleteDirectorTest");
    }

    @Test
    public void updateDirector() {
        // First, create a director
        Director director = new Director("updateDirectorTest", "someSurname", "American");

        ResponseEntity<Director> responseOnCreated = testRestTemplate.withBasicAuth("admin", "password")
                .postForEntity("/api/v1/directors/create", director, Director.class);
        assertThat(responseOnCreated.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Check whether the application properly stored the director
        ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity("/api/v1/directors/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        List <String> returnedNames = path.get("name");
        assertThat(returnedNames).isNotNull().isNotEmpty().contains("updateDirectorTest");

        // Update director
        director = responseOnCreated.getBody();
        director.setName("updateDirectorTestNewName");

        HttpEntity<Director> httpEntity = new HttpEntity<>(director, new HttpHeaders());
        ResponseEntity<Director> responseOnUpdated = testRestTemplate.withBasicAuth("admin", "password")
                .exchange("/api/v1/directors/update/" + director.getId(), HttpMethod.PUT, httpEntity, Director.class);
        assertThat(responseOnUpdated.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Get list of all directors to be sure
        ResponseEntity<String> responseAfterUpdate = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity("/api/v1/directors/all", String.class);

        assertThat(responseAfterUpdate.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath pathAfterUpdate = new JsonPath(responseAfterUpdate.getBody());

        List <String> returnedNamesAfterUpdate = pathAfterUpdate.get("name");
        assertThat(returnedNamesAfterUpdate).isNotNull().isNotEmpty().contains("updateDirectorTestNewName");
    }
}
