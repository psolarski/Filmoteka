package pl.filmoteka.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pl.filmoteka.exception.InvalidExternalApiResponseException;
import pl.filmoteka.model.integration.MusicAlbum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper utility to map response from Spotify API to desirable structure represented by MusicAlbum.
 */
@Component
public class SpotifySearchAlbumMapper {

    /**
     * Map music album list from given json response from Spotify API.
     *
     * @param responseJson Response from Spotify API as normal string
     * @return Processed music albums list
     * @throws InvalidExternalApiResponseException Malformed response from Spotify API
     */
    public List<MusicAlbum> from(String responseJson) throws InvalidExternalApiResponseException {
        ObjectMapper mapper = new ObjectMapper();
        List<MusicAlbum> albums = new ArrayList<>();

        try {
            JsonNode node = mapper.readTree(responseJson);

            // Get number of found products on Spotify
            Integer noOfResults = node.get("albums").get("total").asInt();

            if (noOfResults > 0) {
                albums = readMusicAlbumsFromJsonResponse(node.get("albums").withArray("items"));
            }

        } catch (IOException e) {
            throw new InvalidExternalApiResponseException("Response from Spotify API was malformed!", e);
        }

        return albums;
    }

    /**
     * Read music albums received from Spotify API as JSON.
     *
     * @param rootNode Root node of received JSON
     * @return List of music albums
     */
    private List<MusicAlbum> readMusicAlbumsFromJsonResponse(JsonNode rootNode) {
        List<MusicAlbum> albums = new ArrayList<>();

        rootNode.forEach(ma -> {
            MusicAlbum album = new MusicAlbum();
            album.setTitle(ma.get("name").asText());
            album.setArtistLink(ma.get("artists").get(0).get("external_urls").get("spotify").asText());
            album.setArtistName(ma.get("artists").get(0).get("name").asText());
            album.setDesktopApplicationLink(ma.get("uri").asText());
            album.setWebsiteApplicationLink(ma.get("external_urls").get("spotify").asText());
            album.setImageUrl(ma.withArray("images").get(0).get("url").asText());

            albums.add(album);
        });

        return albums;
    }
}
