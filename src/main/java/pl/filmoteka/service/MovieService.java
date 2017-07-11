package pl.filmoteka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import pl.filmoteka.model.Movie;
import pl.filmoteka.model.User;
import pl.filmoteka.repository.MovieRepository;
import pl.filmoteka.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.mail.SimpleMailMessage;


/**
 * Movie Service
 */
@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    JavaMailSenderImpl mailSender;


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
            message.setSubject("New Movie");
            message.setText("Dear " + user.getLogin() + " new movie " +
                            createdMovie.getName() + " has been added to our " +
                            "system, hope you will like it.");
            mailSender.send(message);
        });
    }
}