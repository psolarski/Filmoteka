package pl.filmoteka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.model.Movie;
import pl.filmoteka.model.User;
import pl.filmoteka.model.integration.NytCriticReview;
import pl.filmoteka.repository.MovieRepository;
import pl.filmoteka.repository.NytCriticReviewRepository;
import pl.filmoteka.repository.UserRepository;

import javax.transaction.Transactional;
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
    private UserRepository userRepository;

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Value("${api.key.nyt}")
    private String nytApiKey;

    @Value("${api.url.nyt.review}")
    private String nytApiUrl;

    @Autowired
    private RestTemplate restTemplate;

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

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}