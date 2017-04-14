package pl.filmoteka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.filmoteka.configuration.MyBean;
import pl.filmoteka.model.Actor;
import pl.filmoteka.repository.ActorRepository;
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

    @Autowired
    private MyBean myBean;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Actor> findAll() {

        System.out.println();
        return actorService.findAllActors();
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Actor createActor(@RequestBody Actor actor) {
        Actor savedActor = actorService.addNewActor(actor);
        return savedActor;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public void testActor(@RequestParam(value = "id") Long id) {
            actorService.deleteActor(id);
    }

}
