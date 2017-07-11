package pl.filmoteka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public List<Director> findAll() {
        return directorService.findAllDirectors();
    }

    @RequestMapping(value = "name", method = RequestMethod.GET)
    public List<Director> findByName(@RequestParam(value = "name") String name) {
        return directorService.findByName(name);
    }

    @RequestMapping(value = "surname", method = RequestMethod.GET)
    public List<Director> findBySurname(@RequestParam(value = "surname") String surname) {
        return directorService.findBySurname(surname);
    }

    @RequestMapping(value = "nameorsurname", method = RequestMethod.GET)
    public List<Director> findByNameOrSurname(@RequestParam(value = "name") String name,
                                              @RequestParam(value = "surname") String surname
    ) {
        return directorService.findByNameOrSurname(name, surname);
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Director createDirector(@RequestBody Director director) {
        return directorService.addNewDirector(director);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public void deleteDirector(@RequestParam(value = "id") Long id) {
        directorService.deleteDirector(id);
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Director> updateDirector(@PathVariable("id") long id, @RequestBody Director modifiedDirector) {
        // Get already stored director with given id
        Director storedDirector = directorService.find(id);

        if (storedDirector == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Explicitly set passed values
        storedDirector.setName(modifiedDirector.getName());
        storedDirector.setSurname(modifiedDirector.getSurname());
        storedDirector.setNationality(modifiedDirector.getNationality());
        storedDirector.setMovies(modifiedDirector.getMovies());

        directorService.updateDirector(storedDirector);

        return new ResponseEntity<>(storedDirector, HttpStatus.OK);
    }
}
