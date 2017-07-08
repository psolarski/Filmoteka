package pl.filmoteka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.filmoteka.model.User;
import pl.filmoteka.repository.UserRepository;

import java.util.List;

/**
 * Service class for user management.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public List<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Transactional
    public List<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User createUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.delete(id);
    }
}
