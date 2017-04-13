package pl.filmoteka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.filmoteka.Configuration.MyBean;
import pl.filmoteka.model.Actor;
import pl.filmoteka.repository.ActorRepository;
import java.util.List;

/**
 * Created by Piotr on 05.04.2017.
 */
@RestController
@RequestMapping("api/v1/actors/")
public class ActorsController {

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private MyBean myBean;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Actor> findAll() {

        System.out.println();
        return actorRepository.findAll();
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Actor createActor(@RequestBody Actor actor) {
        Actor savedActor = actorRepository.saveAndFlush(actor);
        return savedActor;
    }

    @RequestMapping(value = "testnum", method = RequestMethod.GET)
    public void testActor(@RequestParam(value = "someint", required = false) Integer num) {
            System.out.println("num");
    }

}
