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
import pl.filmoteka.model.integration.NytCriticReview;
import pl.filmoteka.model.integration.NytResponse;
import pl.filmoteka.repository.NytCriticReviewRepository;
import pl.filmoteka.repository.NytCriticReviewRepositoryImpl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class NytCriticReviewRepositoryTests {

    @Mock
    private RestTemplate mockedRestTemplate;

    @InjectMocks
    private NytCriticReviewRepository sut = new NytCriticReviewRepositoryImpl();

    private String fakeMovieName = "fakeMovieName";

    @Before
    public void init() {
        mockApiUrl("fakeUrl");
    }

    private void mockApiUrl(String url) {
        ReflectionTestUtils.setField(sut, "nytApiUrl", url);
    }

    @Test
    public void emptyListIfNoReviewsWereFound()
            throws InvalidApplicationConfigurationException {

        NytResponse response = new NytResponse(new ArrayList<NytCriticReview>() {});
        Mockito.when(mockedRestTemplate.getForObject(
                Matchers.any(URI.class), Matchers.any(Class.class))
        ).thenReturn(response);

        assertThat(sut.findByMovieName(fakeMovieName)).isNotNull().isEmpty();
    }

    @Test
    public void oneItemInListIfOneReviewWasFound()
            throws InvalidApplicationConfigurationException {

        NytResponse response = new NytResponse(new ArrayList<>(Arrays.asList(new NytCriticReview())));
        Mockito.when(mockedRestTemplate.getForObject(
                Matchers.any(URI.class), Matchers.any(Class.class))
        ).thenReturn(response);

        assertThat(sut.findByMovieName(fakeMovieName)).isNotNull().hasSize(1);
    }

    @Test
    public void threeItemsInListIfThreeReviewsWereFound()
            throws InvalidApplicationConfigurationException {

        NytResponse response = new NytResponse(new ArrayList<>(
                Arrays.asList(new NytCriticReview(), new NytCriticReview(), new NytCriticReview()))
        );
        Mockito.when(mockedRestTemplate.getForObject(
                Matchers.any(URI.class), Matchers.any(Class.class))
        ).thenReturn(response);

        assertThat(sut.findByMovieName(fakeMovieName)).isNotNull().hasSize(3);
    }

    @Test(expected = InvalidApplicationConfigurationException.class)
    public void failIfInvalidUriToSearchForReviews() throws InvalidApplicationConfigurationException {
        mockApiUrl(" źżćąś // \\ ");

        sut.findByMovieName(fakeMovieName);
    }

    @Test(expected = InvalidApplicationConfigurationException.class)
    public void failIfConnectionErrorWhileGettingReviews() throws InvalidApplicationConfigurationException {
        Mockito.when(mockedRestTemplate.getForObject(
                Matchers.any(URI.class), Matchers.any(Class.class))
        ).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        sut.findByMovieName(fakeMovieName);
    }
}
