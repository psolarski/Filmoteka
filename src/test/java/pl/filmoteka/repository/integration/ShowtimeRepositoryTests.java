package pl.filmoteka.repository.integration;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.model.integration.Showtime;
import pl.filmoteka.repository.ShowtimeRepository;
import pl.filmoteka.repository.ShowtimeRepositoryImpl;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ShowtimeRepositoryTests {
    
    @Mock
    private RestTemplate mockedRestTemplate;
    
    @InjectMocks
    private ShowtimeRepository sut = new ShowtimeRepositoryImpl();

    private Long fakeCinemaId = 1L;
    
    @Before
    public void init() {
        mockApiUrl("fakeUrl");
    }

    private void mockApiUrl(String url) {
        ReflectionTestUtils.setField(sut, "cinemaShowtimesListUrl", url);
    }


    @Test
    public void emptySetIfNoShowtimesWereFound()
            throws InvalidApplicationConfigurationException {
        Mockito.when(mockedRestTemplate.getForObject(
                Matchers.any(URI.class), Matchers.any(Class.class))
        ).thenReturn(new Showtime[]{});

        assertThat(sut.findShowtimesByCinemaId(fakeCinemaId)).isNotNull().isEmpty();
    }

    @Test
    public void oneItemInSetIfOneShowtimeWasFound()
            throws InvalidApplicationConfigurationException {
        Mockito.when(mockedRestTemplate.getForObject(
                Matchers.any(URI.class), Matchers.any(Class.class))
        ).thenReturn(new Showtime[]{new Showtime()});

        assertThat(sut.findShowtimesByCinemaId(fakeCinemaId)).isNotNull().hasSize(1);
    }

    @Test
    public void threeItemsInSetIfThreeShowtimesWereFound()
            throws InvalidApplicationConfigurationException {
        Mockito.when(mockedRestTemplate.getForObject(
                Matchers.any(URI.class), Matchers.any(Class.class))
        ).thenReturn(new Showtime[]{new Showtime(), new Showtime(), new Showtime()});

        assertThat(sut.findShowtimesByCinemaId(fakeCinemaId)).isNotNull().hasSize(3);
    }

    @Test(expected = InvalidApplicationConfigurationException.class)
    public void failToGetShowtimesIfConnectionFailed()
            throws InvalidApplicationConfigurationException {
        Mockito.when(mockedRestTemplate.getForObject(
                Matchers.any(URI.class), Matchers.any(Class.class))
        ).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        sut.findShowtimesByCinemaId(fakeCinemaId);
    }

    @Test(expected = InvalidApplicationConfigurationException.class)
    public void failToGetShowtimesIfUriIsInvalid()
            throws InvalidApplicationConfigurationException {
        mockApiUrl("ąśżźć żź214");

        sut.findShowtimesByCinemaId(fakeCinemaId);
    }
}
