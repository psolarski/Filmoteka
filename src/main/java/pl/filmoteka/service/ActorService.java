package pl.filmoteka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.filmoteka.model.Actor;
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
}
