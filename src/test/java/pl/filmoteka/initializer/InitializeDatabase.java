package pl.filmoteka.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.filmoteka.model.*;
import pl.filmoteka.repository.ActorRepository;
import pl.filmoteka.repository.DirectorRepository;
import pl.filmoteka.repository.MovieRepository;
import pl.filmoteka.repository.RoleRepository;
import pl.filmoteka.service.UserService;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private UserService userService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void initialize() {
        // Actors
        for (int i = 0; i < actorsSize; i++) {
            Actor actor = new Actor("name" + i, "surname" + i, "PL");
            actorRepository.saveAndFlush(actor);
        }

        // Roles
        Role adminRole = new Role("ADMIN");
        Role userRole = new Role("USER");
        roleRepository.saveAndFlush(adminRole);
        roleRepository.saveAndFlush(userRole);

        // Users
        User adminUser = new User("admin", "password", "admin@user.20ak3e");
        User normalUser = new User("user", "password", "user@user.20ak3e");
        adminUser.assignRole(adminRole);
        adminUser.assignRole(userRole);
        normalUser.assignRole(userRole);
        userService.createUser(adminUser);
        userService.createUser(normalUser);

        for (int i = 0; i < usersSize; i++) {
            User user = new User("username" + i, "password", "doo" + i + "@bee.doo");
            userService.createUser(user);
        }

        // Directors
        List<Director> directorList = new ArrayList<>();
        for (int i = 0; i < directorsSize; i++) {
            Director director = new Director("name" + i, "surname" + i, "American");
            directorList.add(directorRepository.saveAndFlush(director));
        }

        // Movies
        for (int i = 0; i < moviesSize; i++) {
            Movie movie = new Movie(
                    "name" + i,
                    100 + i * 3,
                    "genre" + i,
                    LocalDate.now().minusWeeks(i * 2),
                    "English");
            movie.setDirector(directorList.get(i));
            movieRepository.saveAndFlush(movie);
        }
    }
}
