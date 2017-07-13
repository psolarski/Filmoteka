package pl.filmoteka.controller.actor;

import com.jayway.restassured.path.json.JsonPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import pl.filmoteka.model.Actor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Piotr on 13.04.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ActorControllerFullIntegrationTest {

    @Value("${test.db.initializer.actors.size}")
    private Integer actorsSize;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void ensureThatAllActorsAreReturnedFromEndpoint() {
        ResponseEntity<String> allActorsResponse = testRestTemplate.withBasicAuth("user", "password").getForEntity("/api/v1/actors/all", String.class);

        assertThat(allActorsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath jsonPath = new JsonPath(allActorsResponse.getBody());
        List<String> namesList = jsonPath.get("name");
        assertThat(namesList).isNotNull()
                             .isNotEmpty();

        List<String> surnamesList = jsonPath.get("surnames");
        assertThat(surnamesList).isNotNull()
                                .isNotEmpty();

        List<Integer> idList = jsonPath.get("id");
        idList.forEach(s -> assertThat(s).isNotNull());
    }

    @Test
    public void getActorsByName() {
        ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity("/api/v1/actors/name?name=name7", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        assertThat((List<?>) path.get("name")).isNotNull().isNotEmpty()
                .hasSize(1);
    }

    @Test
    public void getActorsBySurname() {
        ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity("/api/v1/actors/surname?surname=surname7", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        assertThat((List<?>) path.get("surname")).isNotNull().isNotEmpty()
                .hasSize(1);
    }

    @Test
    public void getActorsByNameOrSurname() {
        ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity("/api/v1/actors/nameorsurname?name=name7&surname=surname8", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        assertThat((List<?>) path.get("name")).isNotNull().isNotEmpty()
                .hasSize(2);
    }

    @Test
    public void createActor() {
        Actor actor = new Actor("createActorTest", "someSurname", "American");

        ResponseEntity<Actor> responseOnCreated = testRestTemplate.withBasicAuth("admin", "password")
                .postForEntity("/api/v1/actors/create", actor, Actor.class);
        assertThat(responseOnCreated.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actor.equals(responseOnCreated.getBody()));
    }

    @Test
    public void deleteDirector() {
        // First, create an actor
        Actor actor = new Actor("deleteActorTest", "someSurname", "American");

        ResponseEntity<Actor> responseOnCreated = testRestTemplate.withBasicAuth("admin", "password")
                .postForEntity("/api/v1/actors/create", actor, Actor.class);
        assertThat(responseOnCreated.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Then delete it
        testRestTemplate.withBasicAuth("admin", "password")
                .delete("/api/v1/actors/delete?id=" + responseOnCreated.getBody().getId());

        ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity("/api/v1/actors/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        List <String> returnedNames = path.get("name");
        assertThat(returnedNames).isNotNull().isNotEmpty().doesNotContain("deleteActorTest");
    }

    @Test
    public void updateActor() {
        // First, create an actor
        Actor actor = new Actor("updateActorTest", "someSurname", "American");

        ResponseEntity<Actor> responseOnCreated = testRestTemplate.withBasicAuth("admin", "password")
                .postForEntity("/api/v1/actors/create", actor, Actor.class);
        assertThat(responseOnCreated.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Check whether the application properly stored the actor
        ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity("/api/v1/actors/all", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath path = new JsonPath(response.getBody());

        List <String> returnedNames = path.get("name");
        assertThat(returnedNames).isNotNull().isNotEmpty().contains("updateActorTest");

        // Update actor
        actor = responseOnCreated.getBody();
        actor.setName("updateActorTestNewName");

        HttpEntity<Actor> httpEntity = new HttpEntity<>(actor, new HttpHeaders());
        ResponseEntity<Actor> responseOnUpdated = testRestTemplate.withBasicAuth("admin", "password")
                .exchange("/api/v1/actors/update/" + actor.getId(), HttpMethod.PUT, httpEntity, Actor.class);
        assertThat(responseOnUpdated.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Get list of all actors to be sure
        ResponseEntity<String> responseAfterUpdate = testRestTemplate.withBasicAuth("admin", "password")
                .getForEntity("/api/v1/actors/all", String.class);

        assertThat(responseAfterUpdate.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath pathAfterUpdate = new JsonPath(responseAfterUpdate.getBody());

        List <String> returnedNamesAfterUpdate = pathAfterUpdate.get("name");
        assertThat(returnedNamesAfterUpdate).isNotNull().isNotEmpty().contains("updateActorTestNewName");
    }
}
