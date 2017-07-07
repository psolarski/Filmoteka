package pl.filmoteka.controller.actor;

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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Piotr on 13.04.2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ComponentScan("pl.filmoteka")
public class ActorControllerFullIntegrationTest {

    @Value("${test.db.initializer.actors.size}")
    private Integer actorsSize;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void ensureThatAllActorsAreReturnedFromEndpoint() {

        ResponseEntity<String> allActorsResponse = testRestTemplate.withBasicAuth("user", "password").getForEntity("/api/v1/actors/", String.class);

        assertThat(allActorsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonPath jsonPath = new JsonPath(allActorsResponse.getBody());
        List<String> namesList = jsonPath.get("name");
        assertThat(namesList).isNotNull()
                             .isNotEmpty()
                             .hasSize(actorsSize);

        List<String> surnamesList = jsonPath.get("surnames");
        assertThat(surnamesList).isNotNull()
                                .isNotEmpty()
                                .hasSize(actorsSize);

        List<Integer> idList = jsonPath.get("id");
        idList.forEach(s -> assertThat(s).isNotNull());
    }
}
