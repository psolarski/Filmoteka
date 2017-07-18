package pl.filmoteka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.filmoteka.model.Movie;
import pl.filmoteka.model.Rating;
import pl.filmoteka.repository.MovieRepository;
import pl.filmoteka.repository.RatingRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Rating Service
 */
@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Transactional
    public Rating find(Long id) {
        return ratingRepository.findOne(id);
    }

    @Transactional
    public List<Rating> findAllRatings() {
        return ratingRepository.findAll();
    }

    @Transactional
    public Rating addNewRating(Rating rating, Long id) {
        Movie movie = movieRepository.findOne(id);
        rating.setMovie(movie);
        return ratingRepository.saveAndFlush(rating);
    }

    @Transactional
    public Rating updateRating(Long id, Rating rating) {
        Rating storedRating = ratingRepository.findOne(id);

        storedRating.setEvaluation(rating.getEvaluation());
        return ratingRepository.saveAndFlush(storedRating);
    }

    @Transactional
    public void deleteRating(Long id) {
        ratingRepository.delete(id);
    }
}
