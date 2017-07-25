package pl.filmoteka.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity that represents a degree of similarity between two users based on movies watched by them.
 */
// There's no @Entity!
public class SimilarityDegree {

    private Set<Movie> moviesInCommon = new HashSet<>();

    private float howSimilar;

    public SimilarityDegree() {
    }

    public SimilarityDegree(Set<Movie> moviesInCommon, float howSimilar) {
        this.moviesInCommon = moviesInCommon;
        this.howSimilar = howSimilar;
    }

    public Set<Movie> getMoviesInCommon() {
        return moviesInCommon;
    }

    public void setMoviesInCommon(Set<Movie> moviesInCommon) {
        this.moviesInCommon = moviesInCommon;
    }

    public float getHowSimilar() {
        return howSimilar;
    }

    public void setHowSimilar(float howSimilar) {
        this.howSimilar = howSimilar;
    }
}
