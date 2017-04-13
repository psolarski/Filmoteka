package pl.filmoteka.actor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import pl.filmoteka.model.Actor;
import pl.filmoteka.repository.ActorRepository;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

/**
 * Created by Piotr on 13.04.2017.
 */

@RunWith(SpringRunner.class)
@DataJpaTest
public class ActorRepositoryMethodTests {

    private static final String name = "Jan";
    private static final String surname = "Kowalski";

    @Autowired
    private ActorRepository actorRepository;

    @Test
    @Rollback
    public void testFindByName() {
        Actor actor = new Actor(name, "Nowak", "PL");
        actorRepository.saveAndFlush(actor);

        List<Actor> actorsList = actorRepository.findByName(name);
        assertThat(actorsList).hasSize(1)
                              .contains(actor);
    }

    @Test
    @Rollback
    public void testFindBySurname() {
        Actor actor =  new Actor("Jan", surname, "UK");
        actorRepository.saveAndFlush(actor);

        List<Actor> actorList = actorRepository.findBySurname(surname);
        assertThat(actorList).hasSize(1)
                             .contains(actor);
    }

    @Test
    @Rollback
    public void testFindByNameOrSurname() {
        Actor actor = new Actor(name, surname, "UK");
        actorRepository.saveAndFlush(actor);

        List<Actor> actorList = actorRepository.findByNameOrSurname("invalid", surname);
        assertThat(actorList).hasSize(1)
                .contains(actor);
    }

    @Test
    @Rollback
    public void testFindByNameOrSurname2() {
        Actor actor =  new Actor(name, surname, "UK");
        actorRepository.saveAndFlush(actor);

        List<Actor> actorList = actorRepository.findByNameOrSurname(name, "invalid");
        assertThat(actorList).hasSize(1)
                .contains(actor);
    }

}
