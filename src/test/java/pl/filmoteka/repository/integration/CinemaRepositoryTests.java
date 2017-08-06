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
import pl.filmoteka.model.integration.Cinema;
import pl.filmoteka.repository.CinemaRepository;
import pl.filmoteka.repository.CinemaRepositoryImpl;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CinemaRepositoryTests {

    @Mock
    private RestTemplate mockedRestTemplate;

    @InjectMocks
    private CinemaRepository sut = new CinemaRepositoryImpl();

    private String fakePostCode = "dummyPostcode";

    @Before
    public void init() {
        mockApiUrl("/url/dummy/{postcode}");
    }

    private void mockApiUrl(String url) {
        ReflectionTestUtils.setField(sut, "cinemaListUrl", url);
    }

    @Test
    public void emptySetIfNoCinemasWereFound()
            throws InvalidApplicationConfigurationException {
        Mockito.when(mockedRestTemplate.getForObject(
                Matchers.any(URI.class), Matchers.any(Class.class))
        ).thenReturn(new Cinema[]{});

        assertThat(sut.findCinemasByPostcode(fakePostCode)).isNotNull().isEmpty();
    }

    @Test
    public void oneItemInSetIfOneCinemaWasFound()
            throws InvalidApplicationConfigurationException {
        Mockito.when(mockedRestTemplate.getForObject(
                Matchers.any(URI.class), Matchers.any(Class.class))
        ).thenReturn(new Cinema[]{new Cinema()});

        assertThat(sut.findCinemasByPostcode(fakePostCode)).isNotNull().hasSize(1);
    }

    @Test
    public void threeItemsInSetIfThreeCinemasWereFound()
            throws InvalidApplicationConfigurationException {
        Mockito.when(mockedRestTemplate.getForObject(
                Matchers.any(URI.class), Matchers.any(Class.class))
        ).thenReturn(new Cinema[]{new Cinema(), new Cinema(), new Cinema()});

        assertThat(sut.findCinemasByPostcode(fakePostCode)).isNotNull().hasSize(3);
    }

    @Test(expected = InvalidApplicationConfigurationException.class)
    public void failToGetCinemasIfConnectionFailed()
            throws InvalidApplicationConfigurationException {
        Mockito.when(mockedRestTemplate.getForObject(
                Matchers.any(URI.class), Matchers.any(Class.class))
        ).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        sut.findCinemasByPostcode(fakePostCode);
    }

    @Test(expected = InvalidApplicationConfigurationException.class)
    public void failToGetCinemasIfUriIsInvalid()
            throws InvalidApplicationConfigurationException {
        mockApiUrl("ąśżźć żź214");

        sut.findCinemasByPostcode(fakePostCode);
    }
}
