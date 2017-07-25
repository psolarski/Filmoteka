package pl.filmoteka.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Entity that represents Rating
 */
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property="@ratingId")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rating_id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {})
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Min(1)
    @Max(10)
    private int evaluation;

    public Rating(Movie movie, int evaluation) {
        this.movie = movie;
        this.evaluation = evaluation;
    }

    public Rating() {
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public int hashCode() {
        return new HashCodeBuilder().append(id)
                .append(evaluation)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Rating{" +
                "evaluation = " + evaluation +
                '}';
    }
}
