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

    private GreenMail greenMail;

    @Before
    public void testSmtpInit(){
        greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.start();
    }

    @Test
    public void ensureThatUserWillReceiveEmailAfterMovieCreationWithMatchingGenre() throws MessagingException {
        Movie movie = new Movie(
                "EpicTestNewMovie",
                100,
                "horror",
                LocalDate.now().minusWeeks(2),
                "English");
        movie.setDirector(new Director("name", "surname", "American"));

        User user = new User("MailTest", "password", "tmp@jmail.ovh");
        user.setMovies(Stream.of(movie).collect(Collectors.toSet()));
        userRepository.saveAndFlush(user);

        moviesController.createMovie(movie);

        Message[] messages = greenMail.getReceivedMessages();
        assertEquals(1, messages.length);
        assertEquals("New Movie", messages[0].getSubject());

        /* cleaning */
        userRepository.delete(user);
    }

    @After
    public void cleanup(){
        greenMail.stop();
    }
}
