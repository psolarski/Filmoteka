package pl.filmoteka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.exception.InvalidResourceRequestedException;
import pl.filmoteka.model.Role;
import pl.filmoteka.model.SimilarityDegree;
import pl.filmoteka.model.User;
import pl.filmoteka.service.RoleService;
import pl.filmoteka.service.UserService;

import java.util.List;
import java.util.logging.Logger;

/**
 * Controller for user management.
 */
@RestController
@RequestMapping("api/v1/users/")
public class UsersController {

    // Logger
    final static Logger logger = Logger.getLogger(UsersController.class.getName());

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public List<User> findAll() {
        return userService.findAllUsers();
    }

    @RequestMapping(value = "username", method = RequestMethod.GET)
    public User findByLogin(@RequestParam(value = "username") String username) {
        return userService.findByUsername(username);
    }

    @RequestMapping(value = "email", method = RequestMethod.GET)
    public List<User> findByEmail(@RequestParam(value = "email") String email) {
        return userService.findByEmail(email);
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public void deleteUser(@RequestParam(value = "id") Long id) {
        userService.deleteUser(id);
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User modifiedUser) {
        // Get already stored user with given id
        User storedUser = userService.find(id);

        if (storedUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Explicitly set passed values
        storedUser.setEmail(modifiedUser.getEmail());
        storedUser.setMovies(modifiedUser.getMovies());

        User updatedEntity = userService.updateUser(storedUser);

        return new ResponseEntity<>(updatedEntity, HttpStatus.OK);
    }

    @RequestMapping(value = "{user_id}/assignrole", method = RequestMethod.POST)
    public ResponseEntity<User> assignRole(@PathVariable("user_id") Long id, @RequestBody Role role) {
        User user = userService.find(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        role = roleService.find(role.getName());

        if (role == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        user.assignRole(role);
        User updatedEntity = userService.updateUser(user);

        return new ResponseEntity<>(updatedEntity, HttpStatus.OK);
    }

    /**
     * Calculate degree of similarity between two users based on their watched movies list.
     *
     * @param otherUserId ID of chosen user to be compared to
     * @return Similarity degree between two users
     */
    @RequestMapping(value = "similarity/{other_user_id}", method = RequestMethod.GET)
    public ResponseEntity<SimilarityDegree> specifySimilarityDegreeBasedOnWatchedMovies(
            @PathVariable("other_user_id") Long otherUserId) {
        String loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            return new ResponseEntity<>(
                    userService.specifySimilarityDegreeBasedOnWatchedMovies(loggedUsername, otherUserId),
                    HttpStatus.OK
            );

        } catch (InvalidApplicationConfigurationException e) {
            logger.severe("Invalid logged in user");

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (InvalidResourceRequestedException e) {
            logger.severe("There's no user with requested ID");

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Add chosen movie to user's watched movies list.
     *
     * @param id Chosen movie's ID
     * @return Response with updated user information
     */
    @RequestMapping(value = "{movie_id}/watched", method = RequestMethod.GET)
    public ResponseEntity<User> addMovieToWatched(@PathVariable("movie_id") Long id) {
        String loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            return new ResponseEntity<>(userService.addMovieToWatched(loggedUsername, id), HttpStatus.OK);

        } catch (InvalidApplicationConfigurationException e) {
            logger.severe("Invalid logged in user");

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (InvalidResourceRequestedException e) {
            logger.severe("There's no user with requested ID");

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
