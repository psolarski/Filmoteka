package pl.filmoteka.model.comparator;

import pl.filmoteka.model.Movie;

import java.util.Comparator;

/**
 * Comparator for movie entity that compares two movies by their release dates.
 */
public class MovieReleaseDateComparator implements Comparator<Movie> {

    /**
     * Compare two movies by their release dates - sort descending.
     *
     * @param firstMovie First movie entity to compare
     * @param otherMovie Other movie entity to compare
     * @return 1 if first movie is released after the other movie, -1 otherwise, 0 if released on same date
     */
    @Override
    public int compare(Movie firstMovie, Movie otherMovie) {
        return firstMovie.getReleaseDate().compareTo(otherMovie.getReleaseDate()) * -1;
    }
}
