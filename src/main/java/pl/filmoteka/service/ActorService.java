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
    public Actor find(Long id) {
        return actorRepository.findOne(id);
    }

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
    public void addMovie(Long id, Movie movie) {
        Actor actor = actorRepository.findOne(id);
        actor.getMovies().add(movie);
        actorRepository.saveAndFlush(actor);
    }

    @Transactional
    public List<Actor> findActorByName(String name) {
        return actorRepository.findByName(name);
    }

    @Transactional
    public List<Actor> findActorBySurname(String surname) {
        return actorRepository.findBySurname(surname);
    }

    @Transactional
    public List<Actor> findActorByNameOrSurname(String name, String surname) {
        return actorRepository.findByNameOrSurname(name, surname);
    }

    @Transactional
    public Actor updateActor(Actor actor) {
        return actorRepository.saveAndFlush(actor);
    }
}
