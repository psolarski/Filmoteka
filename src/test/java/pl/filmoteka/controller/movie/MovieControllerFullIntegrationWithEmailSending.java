package pl.filmoteka.controller.movie;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.filmoteka.controller.MoviesController;
import pl.filmoteka.model.Director;
import pl.filmoteka.model.Movie;
import pl.filmoteka.model.User;
import pl.filmoteka.repository.DirectorRepository;
import pl.filmoteka.repository.MovieRepository;
import pl.filmoteka.repository.UserRepository;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovieControllerFullIntegrationWithEmailSending {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private MoviesController moviesController;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private DirectorRepository directorRepository;

    private Director director;

    private GreenMail greenMail;

    @Before
    public void testSmtpInit(){
        greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.start();

        if (director == null) {
            director = new Director("MailTestDirector", "surname", "African");
            director = directorRepository.saveAndFlush(director);
        }
    }

    @Test
    public void ensureThatUserWillReceiveEmailAfterMovieCreationWithMatchingGenre() throws MessagingException {
        Movie firstMovie = new Movie(
                "EpicTestNewMovie",
                100,
                "horror",
                LocalDate.now().minusWeeks(2),
                "English");
        Movie otherMovie = new Movie(
                "Some other movie 2",
                100,
                "horror",
                LocalDate.now().minusWeeks(20),
                "English");
        firstMovie.setDirector(director);
        otherMovie.setDirector(director);
        moviesController.createMovie(firstMovie);

        User user = new User("MailTest", "password", "tmp@jmail.ovh");
        user.setMovies(Stream.of(firstMovie).collect(Collectors.toSet()));
        userRepository.saveAndFlush(user);

        moviesController.createMovie(otherMovie);

        Message[] messages = greenMail.getReceivedMessages();
        assertEquals(1, messages.length);
        assertEquals("New movie", messages[0].getSubject());

        /* cleaning */
        userRepository.delete(user);
        movieRepository.delete(firstMovie);
    }

    @After
    public void cleanup(){
        greenMail.stop();
    }
}
