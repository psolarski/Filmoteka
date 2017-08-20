package pl.filmoteka.util;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import pl.filmoteka.exception.InvalidExternalApiResponseException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SpotifySearchAlbumMapperTests {

    private SpotifySearchAlbumMapper sut = new SpotifySearchAlbumMapper();

    @Test
    public void emptyListIfJsonWithNoAlbumsWasGiven() throws InvalidExternalApiResponseException {
        String fakeJsonResponse = readExampleResponseJsonFile("example-json-response/spotify/no-albums-found.json");

        assertThat(sut.from(fakeJsonResponse)).isNotNull().isEmpty();
    }

    private String readExampleResponseJsonFile(String url) {
        try {
            return Resources.toString(Resources.getResource(url), Charsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException("Could not read resource " + url);
        }
    }

    @Test
    public void oneItemInListIfJsonWithOneAlbumWasGiven() throws InvalidExternalApiResponseException {
        String fakeJsonResponse = readExampleResponseJsonFile("example-json-response/spotify/one-album-found.json");

        assertThat(sut.from(fakeJsonResponse)).isNotNull().hasSize(1);
    }

    @Test
    public void threeItemsInListIfJsonWithThreeAlbumsWasGiven() throws InvalidExternalApiResponseException {
        String fakeJsonResponse =
                readExampleResponseJsonFile("example-json-response/spotify/three-albums-found.json");

        assertThat(sut.from(fakeJsonResponse)).isNotNull().hasSize(3);
    }

    @Test(expected = InvalidExternalApiResponseException.class)
    public void failIfMalformedJsonWasGiven() throws InvalidExternalApiResponseException {
        String fakeJsonResponse = "doo bee doo!";

        sut.from(fakeJsonResponse);
    }
}
