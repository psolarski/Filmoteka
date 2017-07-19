package pl.filmoteka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.filmoteka.model.User;
import pl.filmoteka.repository.UserRepository;
import pl.filmoteka.util.PasswordEncoderProvider;

import java.util.List;

/**
 * Service class for User management.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoderProvider passwordEncoderProvider;

    @Transactional
    public User find(Long id) {
        return userRepository.findOne(id);
    }

    @Transactional
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public List<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User createUser(User user) {
        user.setPassword(passwordEncoderProvider.getEncoder().encode(user.getPassword()));

        return userRepository.saveAndFlush(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.delete(id);
    }

    @Transactional
    public User updateUser(User user) {
        return userRepository.saveAndFlush(user);
    }
}
