package pl.filmoteka.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity that represents a movie.
 */
@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private int duration;

    @Column
    private String genre;

    @Column
    private LocalDateTime releaseDate;

    @Column
    private String language;

//    @OneToMany(fetch = FetchType.LAZY, targetEntity = Actor.class, mappedBy = "movies")
//    private Collection<Actor> actors = new ArrayList<>();

    public Movie() {
    }

    public Movie(String name, int duration, String genre, LocalDateTime releaseDate, String language) {
        this.name = name;
        this.duration = duration;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    //    public Collection<Actor> getActors() {
//        return actors;
//    }
//
//    public void setActors(Collection<Actor> actors) {
//        this.actors = actors;
//    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
