package pl.filmoteka.repository.integration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.exception.InvalidExternalApiResponseException;
import pl.filmoteka.model.integration.MusicAlbum;
import pl.filmoteka.model.integration.SpotifyToken;
import pl.filmoteka.repository.MusicAlbumRepository;
import pl.filmoteka.repository.MusicAlbumRepositoryImpl;
import pl.filmoteka.util.SpotifySearchAlbumMapper;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class MusicAlbumRepositoryTests {

    @Mock
    private RestTemplate mockedRestTemplate;

    @Mock
    private SpotifySearchAlbumMapper mapper;

    @InjectMocks
    private MusicAlbumRepository sut = new MusicAlbumRepositoryImpl();

    private String fakeMovieName = "fakeMovieName";

    @Before
    public void init() {
        ReflectionTestUtils.setField(sut, "apiUrlSearch", "fakeUrl");
        ReflectionTestUtils.setField(sut, "apiAlbumsLimit", 5);
    }

    @After
    public void cleanUp() {
        configureInvalidSpotifyToken();
    }

    @Test
    public void emptyListIfNoMusicAlbumsWereFound()
            throws InvalidExternalApiResponseException, InvalidApplicationConfigurationException {

        configureValidSpotifyToken();
        Mockito.when(mockedRestTemplate.exchange(
                Matchers.any(URI.class),
                Matchers.eq(HttpMethod.GET),
                Matchers.any(HttpEntity.class),
                Matchers.eq(String.class)
        )).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        Mockito.when(mapper.from(Matchers.any())).thenReturn(new ArrayList<>());

        assertThat(sut.findMusicAlbumByMovieName(fakeMovieName)).isNotNull().isEmpty();
    }

    private void configureValidSpotifyToken() {
        ReflectionTestUtils.setField(sut, "TOKEN_EXPIRES_ON", LocalDateTime.now().plusWeeks(1));
        ReflectionTestUtils.setField(sut, "TOKEN", "fakeToken");
    }

    @Test
    public void emptyListIfNoMusicAlbumsWereFoundAndExpiredToken()
            throws InvalidExternalApiResponseException, InvalidApplicationConfigurationException {

        ResponseEntity<SpotifyToken> fakeTokenResponse = new ResponseEntity<>(
                new SpotifyToken("fakeAccessToken", "fakeTokenType", 3600),
                HttpStatus.OK
        );
        Mockito.when(mockedRestTemplate.exchange(
                Matchers.anyString(),
                Matchers.eq(HttpMethod.POST),
                Matchers.any(HttpEntity.class),
                Matchers.eq(SpotifyToken.class)
        )).thenReturn(fakeTokenResponse);
        Mockito.when(mockedRestTemplate.exchange(
                Matchers.any(URI.class),
                Matchers.eq(HttpMethod.GET),
                Matchers.any(HttpEntity.class),
                Matchers.eq(String.class)
        )).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        Mockito.when(mapper.from(Matchers.any())).thenReturn(new ArrayList<>());

        assertThat(sut.findMusicAlbumByMovieName(fakeMovieName)).isNotNull().isEmpty();
    }

    @Test
    public void oneItemInListIfOneMusicAlbumWasFound()
            throws InvalidExternalApiResponseException, InvalidApplicationConfigurationException {

        configureValidSpotifyToken();
        Mockito.when(mockedRestTemplate.exchange(
                Matchers.any(URI.class),
                Matchers.eq(HttpMethod.GET),
                Matchers.any(HttpEntity.class),
                Matchers.eq(String.class)
        )).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        Mockito.when(mapper.from(Matchers.any())).thenReturn(Collections.singletonList(new MusicAlbum()));

        assertThat(sut.findMusicAlbumByMovieName(fakeMovieName)).isNotNull().hasSize(1);
    }

    @Test
    public void threeItemsInListIfThreeMusicAlbumsWereFound()
            throws InvalidExternalApiResponseException, InvalidApplicationConfigurationException {

        configureValidSpotifyToken();
        Mockito.when(mockedRestTemplate.exchange(
                Matchers.any(URI.class),
                Matchers.eq(HttpMethod.GET),
                Matchers.any(HttpEntity.class),
                Matchers.eq(String.class)
        )).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        Mockito.when(mapper.from(Matchers.any()))
                .thenReturn(Arrays.asList(new MusicAlbum(), new MusicAlbum(), new MusicAlbum())
                );

        assertThat(sut.findMusicAlbumByMovieName(fakeMovieName)).isNotNull().hasSize(3);
    }

    @Test(expected = InvalidExternalApiResponseException.class)
    public void failIfErrorResponseWhileObtainingAuthorizationToken()
            throws InvalidExternalApiResponseException, InvalidApplicationConfigurationException {

        configureInvalidSpotifyToken();
        ResponseEntity<SpotifyToken> fakeTokenResponse = new ResponseEntity<>(
                new SpotifyToken("fakeAccessToken", "fakeTokenType", 3600),
                HttpStatus.BAD_REQUEST
        );
        Mockito.when(mockedRestTemplate.exchange(
                Matchers.anyString(),
                Matchers.eq(HttpMethod.POST),
                Matchers.any(HttpEntity.class),
                Matchers.eq(SpotifyToken.class)
        )).thenReturn(fakeTokenResponse);

        sut.findMusicAlbumByMovieName(fakeMovieName);
    }

    private void configureInvalidSpotifyToken() {
        ReflectionTestUtils.setField(sut, "TOKEN_EXPIRES_ON", LocalDateTime.now().minusWeeks(1));
        ReflectionTestUtils.setField(sut, "TOKEN", "");
    }

    @Test(expected = InvalidApplicationConfigurationException.class)
    public void failIfInvalidUrlToSearchFunctionality()
            throws InvalidExternalApiResponseException, InvalidApplicationConfigurationException {

        configureValidSpotifyToken();
        ReflectionTestUtils.setField(sut, "apiUrlSearch", "ąś żźć /\\");

        sut.findMusicAlbumByMovieName(fakeMovieName);
    }

    @Test(expected = InvalidApplicationConfigurationException.class)
    public void failIfSetInvalidEncodingForUriBuilder()
            throws InvalidExternalApiResponseException, InvalidApplicationConfigurationException {

        configureValidSpotifyToken();
        ReflectionTestUtils.setField(sut, "characterEncoding", "fakeEncoding");

        sut.findMusicAlbumByMovieName(fakeMovieName);
    }

    @Test(expected = InvalidApplicationConfigurationException.class)
    public void failIfHttpClientErrorOccurredWhileGettingAlbumsList()
            throws InvalidExternalApiResponseException, InvalidApplicationConfigurationException {

        configureValidSpotifyToken();
        Mockito.when(mockedRestTemplate.exchange(
                Matchers.any(URI.class),
                Matchers.eq(HttpMethod.GET),
                Matchers.any(HttpEntity.class),
                Matchers.eq(String.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        sut.findMusicAlbumByMovieName(fakeMovieName);
    }

    @Test(expected = RuntimeException.class)
    public void failIfReceivedErrorCodeWhileGettingAlbumsList()
            throws InvalidExternalApiResponseException, InvalidApplicationConfigurationException {

        configureValidSpotifyToken();
        ResponseEntity<String> fakeAlbumsListResponse = new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        Mockito.when(mockedRestTemplate.exchange(
                Matchers.any(URI.class),
                Matchers.eq(HttpMethod.GET),
                Matchers.any(HttpEntity.class),
                Matchers.eq(String.class)
        )).thenReturn(fakeAlbumsListResponse);

        sut.findMusicAlbumByMovieName(fakeMovieName);
    }
}
