package pl.filmoteka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.filmoteka.model.Actor;

import java.util.List;

/**
 * Created by Piotr on 29.03.2017.
 */
public interface ActorRepository extends JpaRepository<Actor, Long> {

    List<Actor> findByName(String name);

    List<Actor> findBySurname(String surname);

    List<Actor> findByNameOrSurname(String name, String surname);
}
