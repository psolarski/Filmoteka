package pl.filmoteka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.exception.InvalidResourceRequestedException;
import pl.filmoteka.model.Actor;
import pl.filmoteka.model.Director;
import pl.filmoteka.model.Movie;
import pl.filmoteka.model.User;
import pl.filmoteka.model.integration.NytCriticReview;
import pl.filmoteka.repository.*;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * movie Service
 */
@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    private NytCriticReviewRepository nytCriticReviewRepository;

    @Transactional
    public Movie find(Long id) {
        return movieRepository.findOne(id);
    }

    @Transactional
    public List<Movie> findAllMovies() {
        return movieRepository.findAll();
    }

    @Transactional
    public List<Movie> findByName(String name) { return movieRepository.findByName(name); }

    @Transactional
    public List<Movie> findByGenre(String genre) { return movieRepository.findByGenre(genre); }

    @Transactional
    public Movie addNewMovie(Movie movie) {
        movieRepository.saveAndFlush(movie);
        sendMail(movie);
        return movie;
    }

    @Transactional
    public void deleteMovie(Long id) {
        movieRepository.delete(id);
    }

    @Transactional
    public Movie updateMovie(Movie movie) {
        return movieRepository.saveAndFlush(movie);
    }

    /**
     * Znajdź wystawione przez krytyków recenzje dla wybranego filmu na podstawie jego id.
     *
     * @param id Numer id wybranego filmu
     * @return Lista recenzji krytyków, przypisana do wybranego filmu
     * @throws InvalidApplicationConfigurationException Dane połączenia z API są nieprawidłowe
     */
    public List<NytCriticReview> findCriticReviewsByMovieId(Long id) throws InvalidApplicationConfigurationException {
        Movie movie = movieRepository.findOne(id);

        return nytCriticReviewRepository.findByMovieName(movie.getName());
    }

    /**
     * Zwróć N najlepiej ocenianych filmów na podstawie ratingu.
     *
     * @param filmLimit liczba filmów, jak ma być zwrócona.
     */
    @Transactional
    public List<Movie> findNBestRatedMovies(int filmLimit) {
        List<Movie> bestRatedMovies = movieRepository.findAll()
                                                     .stream()
                                                     .sorted(Comparator.comparing(
                                                             movies -> movies.getRatings()
                                                             .stream()
                                                             .mapToInt(movie -> movie.getEvaluation())
                                                             .sum())
                                                     ).limit(filmLimit)
                                         .collect(Collectors.toList());
        return bestRatedMovies;
    }

    /**
     * Add chosen movie to user's watched movies list.
     *
     * @param username Logged user name
     * @param movieId Chosen movie's ID
     * @return User with updated information
     * @throws InvalidApplicationConfigurationException No user was found with logged user name
     * @throws InvalidResourceRequestedException Requested movie doesn't exist
     */
    @Transactional
    public User addMovieToWatched(String username, Long movieId)
            throws InvalidApplicationConfigurationException, InvalidResourceRequestedException {
        Movie movie = movieRepository.findOne(movieId);

        if (movie == null) {
            throw new InvalidResourceRequestedException("There\'s no movie with id " + movieId);
        }

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new InvalidApplicationConfigurationException();
        }
        user.addMovieToWatched(movie);
        movie.addUserThatWatchedMovie(user);

        return userRepository.saveAndFlush(user);
    }

    @Transactional
    public Movie assignActorToMovie(Long movieId, Long actorId) throws InvalidResourceRequestedException {
        Movie movie = movieRepository.findOne(movieId);

        if (movie == null) {
            throw new InvalidResourceRequestedException("There\'s no movie with id " + movieId);
        }

        Actor actor = actorRepository.findOne(actorId);

        if (actor == null) {
            throw new InvalidResourceRequestedException("There\'s no actor with id " + movieId);
        }

        movie.assignActor(actor);

        return movieRepository.saveAndFlush(movie);
    }

    @Transactional
    public Movie assignDirectorToMovie(Long movieId, Long directorId) throws InvalidResourceRequestedException {
        Movie movie = movieRepository.findOne(movieId);

        if (movie == null) {
            throw new InvalidResourceRequestedException("There\'s no movie with id " + movieId);
        }

        Director director = directorRepository.findOne(directorId);

        if (director == null) {
            throw new InvalidResourceRequestedException("There\'s no actor with id " + movieId);
        }

        movie.assignDirector(director);

        return movieRepository.saveAndFlush(movie);
    }

    /**
     * Funkcja odpowiedzialna za wysylanie emaila do wszystkich uzytkownikow,
     * ktorzy maja dodane filmy z tego samego gatunku.
     */
    private void sendMail(Movie createdMovie) {
        Predicate<Movie> predicate = movie -> movie.getGenre().equals(createdMovie.getGenre());

        List<User> userList = userRepository.findAll();
        List<User> usersListWithGenre = userList.stream().
                                                 filter(u -> u.getMovies().
                                                                stream().
                                                                anyMatch(predicate)).
                                                 collect(Collectors.toList());
        if(usersListWithGenre != null) {
            usersListWithGenre.forEach(System.out::println);
        }

        /* Send email to users on list */
        SimpleMailMessage message = new SimpleMailMessage();
        usersListWithGenre.forEach(user -> {
            message.setFrom("Filmoteka");
            message.setTo(user.getEmail());
            message.setSubject("New movie");
            message.setText("Dear " + user.getUsername() + " new movie " +
                            createdMovie.getName() + " has been added to our " +
                            "system, hope you will like it.");
            mailSender.send(message);
        });
    }
}