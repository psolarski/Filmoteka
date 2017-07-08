package pl.filmoteka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.filmoteka.model.User;
import pl.filmoteka.service.UserService;

import java.util.List;

/**
 * Controller for user management.
 */
@RestController
@RequestMapping("api/v1/users/")
public class UsersController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public List<User> findAll() {
        return userService.findAllUsers();
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public List<User> findByLogin(@RequestParam(value = "login") String login) {
        return userService.findByLogin(login);
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
}
