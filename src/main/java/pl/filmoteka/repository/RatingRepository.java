package pl.filmoteka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.filmoteka.model.Rating;

/**
 * Repository for Rating entity.
 */
public interface RatingRepository extends JpaRepository<Rating, Long> {
}
