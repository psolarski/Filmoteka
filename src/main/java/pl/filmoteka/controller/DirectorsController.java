package pl.filmoteka.controller;

import org.omg.PortableServer.REQUEST_PROCESSING_POLICY_ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.filmoteka.model.Director;
import pl.filmoteka.service.DirectorService;

import java.util.List;

/**
 * Created by Piotr on 14.04.2017.
 */
@RestController
@RequestMapping("api/v1/directors/")
public class DirectorsController {

    @Autowired
    private DirectorService directorService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Director> findAll() {

        System.out.println();
        return directorService.findAllDirectors();
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Director createDirector(@RequestBody Director director) {
        return directorService.addNewDirector(director);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public void deleteDirector(@RequestParam(value = "id") Long id) {
        directorService.deleteDirector(id);
    }
}
