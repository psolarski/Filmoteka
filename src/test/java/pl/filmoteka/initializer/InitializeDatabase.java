package pl.filmoteka.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.filmoteka.model.Actor;
import pl.filmoteka.model.Director;
import pl.filmoteka.model.Movie;
import pl.filmoteka.model.User;
import pl.filmoteka.repository.ActorRepository;
import pl.filmoteka.repository.DirectorRepository;
import pl.filmoteka.repository.MovieRepository;
import pl.filmoteka.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

/**
 * Class responsible for initialize database before all tests.
 */
@Component
public class InitializeDatabase {

    @Value("${test.db.initializer.actors.size}")
    private Integer actorsSize;

    @Value("${test.db.initializer.users.size}")
    private Integer usersSize;

    @Value("${test.db.initializer.movies.size}")
    private Integer moviesSize;

    @Value("${test.db.initializer.directors.size}")
    private Integer directorsSize;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private DirectorRepository directorRepository;

    @PostConstruct
    public void initialize() {
        // Actors
        for (int i = 0; i < actorsSize; i++) {
            Actor actor = new Actor("name" + i, "surname" + i, "PL");
            actorRepository.saveAndFlush(actor);
        }

        // Users
        for (int i = 0; i < usersSize; i++) {
            User user = new User("username" + i, "password", "doo" + i + "@bee.doo");
            userRepository.saveAndFlush(user);
        }

        // Movies
        for (int i = 0; i < moviesSize; i++) {
            Movie movie = new Movie(
                    "name" + i,
                    100 + i * 3,
                    "genre" + i,
                    LocalDate.now().minusWeeks(i * 2),
                    "English");
            movie.setDirector(new Director("name" + i, "surname" + i, "American"));
            movieRepository.saveAndFlush(movie);
        }

        // Directors
        for (int i = 0; i < directorsSize; i++) {
            Director director = new Director("name" + i, "surname" + i, "American");
            directorRepository.saveAndFlush(director);
        }
    }
}
