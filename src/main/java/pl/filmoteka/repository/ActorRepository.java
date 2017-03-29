package pl.filmoteka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.filmoteka.model.Actor;

/**
 * Created by Piotr on 29.03.2017.
 */
public interface ActorRepository extends JpaRepository<Actor, Long> {
}
