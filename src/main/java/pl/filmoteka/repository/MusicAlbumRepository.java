package pl.filmoteka.repository;

import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.exception.InvalidExternalApiResponseException;
import pl.filmoteka.model.integration.MusicAlbum;

import java.util.List;

/**
 * Custom repository for music album.
 */
public interface MusicAlbumRepository {

    List<MusicAlbum> findMusicAlbumByMovieName(String movieName) throws InvalidExternalApiResponseException, InvalidApplicationConfigurationException;
}
