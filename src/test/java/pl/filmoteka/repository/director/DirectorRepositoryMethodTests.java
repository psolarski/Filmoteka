package pl.filmoteka.repository.director;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import pl.filmoteka.model.Director;
import pl.filmoteka.repository.DirectorRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Piotr on 13.04.2017.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class DirectorRepositoryMethodTests {

    private static final String name = "Peter";
    private static final String surname = "Jackson";

    @Autowired
    private DirectorRepository directorRepository;

    @Test
    @Rollback
    public void testFindByName() {
        Director director = new Director(name, "Nowak", "NZL");
        directorRepository.saveAndFlush(director);

        List<Director> directorsList = directorRepository.findByName(name);
        assertThat(directorsList).hasSize(1)
                .contains(director);
    }

    @Test
    @Rollback
    public void testFindBySurname() {
        Director director =  new Director("Jan", surname, "NZL");
        directorRepository.saveAndFlush(director);

        List<Director> directorList = directorRepository.findBySurname(surname);
        assertThat(directorList).hasSize(1)
                .contains(director);
    }

    @Test
    @Rollback
    public void testFindByNameOrSurname() {
        Director director = new Director(name, surname, "NZL");
        directorRepository.saveAndFlush(director);

        List<Director> directorList = directorRepository.findByNameOrSurname("invalid", surname);
        assertThat(directorList).hasSize(1)
                .contains(director);
    }

    @Test
    @Rollback
    public void testFindByNameOrSurname2() {
        Director director =  new Director(name, surname, "UK");
        directorRepository.saveAndFlush(director);

        List<Director> directorList = directorRepository.findByNameOrSurname(name, "invalid");
        assertThat(directorList).hasSize(1)
                .contains(director);
    }
}
