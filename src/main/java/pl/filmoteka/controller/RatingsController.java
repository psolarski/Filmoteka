package pl.filmoteka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.filmoteka.model.Rating;
import pl.filmoteka.service.RatingService;

import java.util.List;

/**
 * Rating Controller
 */
@RestController
@RequestMapping("api/v1/ratings/")
public class RatingsController {

    @Autowired
    private RatingService ratingService;

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public List<Rating> findAll() {
        return ratingService.findAllRatings();
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public ResponseEntity<Rating> createRating(@RequestBody Rating rating,
                                               @RequestParam(value = "movieId") Long movieId) {
        ratingService.addNewRating(rating, movieId);
        return new ResponseEntity<>(rating, HttpStatus.OK);
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    public void deleteRating(@PathVariable(value = "id") Long id) {
        ratingService.deleteRating(id);
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Rating> updateRating(@PathVariable(value = "id") Long id,
                                               @RequestBody Rating rating) {
        if(rating != null) {
            return new ResponseEntity<>(ratingService.updateRating(id, rating),
                                        HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}