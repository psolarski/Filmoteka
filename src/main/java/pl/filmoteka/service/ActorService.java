package pl.filmoteka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.filmoteka.model.Actor;
import pl.filmoteka.model.Movie;
import pl.filmoteka.repository.ActorRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Piotr on 14.04.2017.
 */
@Service
public class ActorService {

    @Autowired
    ActorRepository actorRepository;

    @Transactional
    public List<Actor> findAllActors() {
        return actorRepository.findAll();
    }

    @Transactional
    public Actor addNewActor(Actor actor) {
        return actorRepository.saveAndFlush(actor);
    }

    @Transactional
    public void deleteActor(Long id) {
        actorRepository.delete(id);
    }

    @Transactional
    public void addFilm(Long id, Movie movie) {
        Actor actor = actorRepository.getOne(id);
//        actor.getMovies().add(movie);
//        for (Movie m: actor.getMovies()) {
//            System.out.println("ELO ELO");
//        }
        actorRepository.saveAndFlush(actor);
    }
}
