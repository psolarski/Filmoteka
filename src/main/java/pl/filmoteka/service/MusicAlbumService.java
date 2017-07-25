package pl.filmoteka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.exception.InvalidExternalApiResponseException;
import pl.filmoteka.exception.InvalidResourceRequestedException;
import pl.filmoteka.model.Movie;
import pl.filmoteka.model.integration.MusicAlbum;
import pl.filmoteka.repository.MovieRepository;
import pl.filmoteka.repository.MusicAlbumRepository;

import java.util.List;

/**
 * Service class for music album.
 */
@Service
public class MusicAlbumService {

    @Autowired
    private MusicAlbumRepository musicAlbumRepository;

    @Autowired
    private MovieRepository movieRepository;

    /**
     * Find music albums on Spotify for given movie by its ID.
     *
     * @param movieId Chosen movie's ID
     * @return List of albums related to chosen movie
     * @throws InvalidResourceRequestedException Movie with chosen id doesn't exist
     * @throws InvalidExternalApiResponseException Malformed response from external API
     * @throws InvalidApplicationConfigurationException Invalid configuration for external API
     */
    public List<MusicAlbum> findMusicAlbumByMovieId(Long movieId)
            throws InvalidResourceRequestedException, InvalidExternalApiResponseException,
            InvalidApplicationConfigurationException {

        Movie movie = movieRepository.findOne(movieId);

        if (movie == null) {
            throw new InvalidResourceRequestedException("There's no movie with id " + movieId);
        }

        return musicAlbumRepository.findMusicAlbumByMovieName(movie.getName());
    }
}
