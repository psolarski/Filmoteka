package pl.filmoteka.repository;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.exception.InvalidExternalApiResponseException;
import pl.filmoteka.model.integration.MusicAlbum;
import pl.filmoteka.model.integration.SpotifyToken;
import pl.filmoteka.util.SpotifySearchAlbumMapper;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

/**
 * Custom repository implementation for music album.
 * Documentation: https://developer.spotify.com/web-api/authorization-guide/
 */
@Repository
public class MusicAlbumRepositoryImpl implements MusicAlbumRepository {

    // Logger
    final static Logger logger = Logger.getLogger(MusicAlbumRepositoryImpl.class.getName());

    // Spotify API Token expiration date
    private static LocalDateTime TOKEN_EXPIRES_ON = LocalDateTime.now();

    // Spotify API Token
    private static String TOKEN = "";

    @Value("${api.spotify.clientid}")
    private String apiClientId;

    @Value("${api.spotify.secretclient}")
    private String apiSecretClient;

    @Value("${api.url.spotify.search}")
    private String apiUrlSearch;

    @Value("${api.url.spotify.token}")
    private String apiUrlToken;

    @Value("${api.misc.spotify.limit}")
    private Integer apiAlbumsLimit;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Find a music album on Spotify for given movie by its name.
     *
     * @param movieName Name of chosen movie
     * @return List of music albums from Spotify related to movie with given name
     * @throws InvalidApplicationConfigurationException Invalid configuration for external API
     * @throws InvalidExternalApiResponseException Malformed response from external API
     */
    @Override
    public List<MusicAlbum> findMusicAlbumByMovieName(String movieName) throws InvalidExternalApiResponseException, InvalidApplicationConfigurationException {
        // First - obtain token from API
        if (TOKEN_EXPIRES_ON.isBefore(LocalDateTime.now()) || TOKEN.isEmpty()) {
            obtainApiToken();
        }

        // Now we can proceed to actually get albums from Spotify
        String headerAuth = "Bearer " + TOKEN;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", headerAuth);

        try {
            // Let's build the URI!
            URIBuilder url = new URIBuilder(apiUrlSearch);
            url.addParameter("q", URLEncoder.encode(movieName, "UTF-8"));
            url.addParameter("type", "album");
            url.addParameter("limit", apiAlbumsLimit.toString());

            ResponseEntity<String> responseAlbums =
                    restTemplate.exchange(url.build(), HttpMethod.GET, new HttpEntity<>(headers), String.class);

            // If response is not OK then stop...
            if (responseAlbums.getStatusCode().value() != 200) {
                throw new RuntimeException();
            }

            return new SpotifySearchAlbumMapper().from(responseAlbums.getBody());

        } catch (URISyntaxException e) {
            logger.severe("Invalid URI");

        } catch (HttpClientErrorException e) {
            logger.severe("Connection error");

        } catch (UnsupportedEncodingException e) {
            logger.severe("Invalid encoding");
        }

        throw new InvalidApplicationConfigurationException();
    }

    /**
     * Obtain necessary API token from Spotify.
     *
     * @throws InvalidExternalApiResponseException Malformed response from external API
     */
    private void obtainApiToken() throws InvalidExternalApiResponseException {
        // Prepare necessary content for POST request
        String decodedAuthString = apiClientId + ":" + apiSecretClient;
        String headerAuth = "Basic " + Base64.getEncoder().encodeToString(decodedAuthString.getBytes(StandardCharsets.UTF_8));

        // Now prepare request headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", headerAuth);

        // This is extremely important! We don't send JSON here!
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // We must set this grant_type to get token
        HttpEntity<String> httpEntity =
                new HttpEntity<>("grant_type=client_credentials", headers);

        ResponseEntity<SpotifyToken> responseToken =
                restTemplate.exchange(apiUrlToken, HttpMethod.POST, httpEntity, SpotifyToken.class);

        // If response is not OK then stop...
        if (responseToken.getStatusCode().value() != 200) {
            throw new InvalidExternalApiResponseException("Response from Spotify API was malformed!");
        }

        // At last let's save received values
        TOKEN = responseToken.getBody().getAccessToken();

        // Minus 60 seconds to be 100% sure that we won't try to make a request with 1 seconds of token left...
        TOKEN_EXPIRES_ON = LocalDateTime.now().plusSeconds(responseToken.getBody().getExpiresIn() - 60);
    }
}
