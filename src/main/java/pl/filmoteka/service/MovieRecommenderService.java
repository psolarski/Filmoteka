package pl.filmoteka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.model.Movie;
import pl.filmoteka.model.User;
import pl.filmoteka.model.comparator.MovieReleaseDateComparator;

import java.util.*;

/**
 * Service used to provide movies recommendations for logged in user.
 */
@Service
public class MovieRecommenderService {

    @Autowired
    private MovieService movieService;

    @Autowired
    private UserService userService;

    @Value("${movie.recommendations.limit}")
    private Integer movieRecommendationsLimit;

    private User loggedUser;
    private Set<String> mostWatchedGenres = new HashSet<>();
    private Map<String, Integer> watchedMovieGenres = new HashMap<>();
    private List<Movie> recommendedMovies = new ArrayList<>();

    /**
     * Provide movie recommendations for logged user based on watched movies.
     *
     * @param username Username of logged in user
     * @return Set of recommended movies
     * @throws InvalidApplicationConfigurationException Requested other user doesn't exist
     */
    @Transactional
    public List<Movie> getRecommendations(String username) throws InvalidApplicationConfigurationException {
        loggedUser = userService.findByUsername(username);

        if (loggedUser == null) {
            throw new InvalidApplicationConfigurationException();
        }

        selectMostWatchedGenres();
        populateRecommendedMoviesCollectionWithLimit();
        removeAlreadyWatchedMoviesFromRecommendations();

        return recommendedMovies;
    }

    private void selectMostWatchedGenres() {
        countWatchedMovieGenres();
        Integer mostWatchedGenreCount = selectBiggestCountValueOfWatchedGenres(watchedMovieGenres);

        watchedMovieGenres.forEach((genre, count) -> {
            if (mostWatchedGenreCount.equals(count)) {
                mostWatchedGenres.add(genre);
            }
        });
    }

    private void countWatchedMovieGenres() {
        loggedUser.getMovies().forEach(movie -> {
            Integer count = watchedMovieGenres.containsKey(movie.getGenre()) ?
                    watchedMovieGenres.get(movie.getGenre()) : 0;
            watchedMovieGenres.put(movie.getGenre(), count + 1);
        });
    }

    private Integer selectBiggestCountValueOfWatchedGenres(Map<String, Integer> watchedMovieGenres) {
        Optional<Map.Entry<String, Integer>> biggestCount = watchedMovieGenres.entrySet()
                .stream().max(Map.Entry.comparingByValue());

        return biggestCount.isPresent() ? biggestCount.get().getValue() : 0;
    }

    private void populateRecommendedMoviesCollectionWithLimit() {
        mostWatchedGenres.forEach(genre -> movieService.findByGenre(genre)
                .forEach(movie -> recommendedMovies.add(movie))
        );

        recommendedMovies.sort(new MovieReleaseDateComparator());
        int moviesLimit = recommendedMovies.size() > movieRecommendationsLimit
                ? movieRecommendationsLimit : recommendedMovies.size();
        recommendedMovies = recommendedMovies.subList(0, moviesLimit);
    }

    private void removeAlreadyWatchedMoviesFromRecommendations() {
        recommendedMovies.removeAll(loggedUser.getMovies());
    }
}