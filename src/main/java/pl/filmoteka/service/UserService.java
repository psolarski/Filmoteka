package pl.filmoteka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.exception.InvalidResourceRequestedException;
import pl.filmoteka.model.Movie;
import pl.filmoteka.model.SimilarityDegree;
import pl.filmoteka.model.User;
import pl.filmoteka.repository.MovieRepository;
import pl.filmoteka.repository.UserRepository;
import pl.filmoteka.util.PasswordEncoderProvider;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service class for user management.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoderProvider passwordEncoderProvider;

    @Autowired
    private MovieRepository movieRepository;

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

    /**
     * Calculate degree of similarity between two users based on their watched movies list.
     *
     * @param loggedUsername User name of logged user
     * @param otherUserId ID of chosen user to be compared to
     * @return Similarity degree between two users
     * @throws InvalidApplicationConfigurationException No user was found with logged user name
     * @throws InvalidResourceRequestedException Requested other user doesn't exist
     */
    @Transactional
    public SimilarityDegree specifySimilarityDegreeBasedOnWatchedMovies(String loggedUsername, Long otherUserId)
            throws InvalidApplicationConfigurationException, InvalidResourceRequestedException {
        User otherUser = userRepository.findOne(otherUserId);

        if (otherUser == null) {
            throw new InvalidResourceRequestedException("There\'s no other user with id " + otherUserId);
        }

        User loggedUser = userRepository.findByUsername(loggedUsername);

        if (loggedUser == null) {
            throw new InvalidApplicationConfigurationException();
        }

        Set<Movie> moviesInCommon = new HashSet<>(loggedUser.getMovies());
        moviesInCommon.retainAll(otherUser.getMovies());

        float howSimilar = moviesInCommon.size() * 1f / loggedUser.getMovies().size() * 1f * 100f;
        SimilarityDegree degree = new SimilarityDegree(moviesInCommon, howSimilar);

        return degree;
    }
}
