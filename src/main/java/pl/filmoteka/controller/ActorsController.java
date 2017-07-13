package pl.filmoteka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.filmoteka.aspect.Monitored;
import pl.filmoteka.model.Actor;
import pl.filmoteka.model.Movie;
import pl.filmoteka.service.ActorService;

import java.util.List;

/**
 * Created by Piotr on 05.04.2017.
 */
@RestController
@RequestMapping("api/v1/actors/")
public class ActorsController {

    @Autowired
    private ActorService actorService;

    @Monitored
    @RequestMapping(value = "all", method = RequestMethod.GET)
    public List<Actor> findAll() {
        return actorService.findAllActors();
    }

    @RequestMapping(value = "name", method = RequestMethod.GET)
    public List<Actor> findByName(@RequestParam(value = "name") String name) {
        return actorService.findActorByName(name);
    }

    @RequestMapping(value = "surname", method = RequestMethod.GET)
    public List<Actor> findBySurname(@RequestParam(value = "surname") String surname) {
        return actorService.findActorBySurname(surname);
    }

    @RequestMapping(value = "nameorsurname", method = RequestMethod.GET)
    public List<Actor> findByNameOrSurname(@RequestParam(value = "name") String name,
                                              @RequestParam(value = "surname") String surname
    ) {
        return actorService.findActorByNameOrSurname(name, surname);
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Actor createActor(@RequestBody Actor actor) {
        Actor savedActor = actorService.addNewActor(actor);
        return savedActor;
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public void deleteActor(@RequestParam(value = "id") Long id) {
            actorService.deleteActor(id);
    }

    @RequestMapping(value = "movie", method = RequestMethod.POST)
    public void addMovie(@RequestParam(value = "id") Long id, @RequestBody Movie movie) {
        actorService.addMovie(id, movie);
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Actor> updateActor(@PathVariable("id") long id, @RequestBody Actor modifiedActor) {
        // Get already stored actor with given id
        Actor storedActor = actorService.find(id);

        if (storedActor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Explicitly set passed values
        storedActor.setName(modifiedActor.getName());
        storedActor.setSurname(modifiedActor.getSurname());
        storedActor.setNationality(modifiedActor.getNationality());
        storedActor.setMovies(modifiedActor.getMovies());

        Actor updatedEntity = actorService.updateActor(storedActor);

        return new ResponseEntity<>(updatedEntity, HttpStatus.OK);
    }
}
