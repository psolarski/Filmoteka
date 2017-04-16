package pl.filmoteka.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    private String nationality;

    /* Na razie porzucam encje, ponieważ coś mi tu nie gra */
//    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Movie.class)
//    @JoinColumn(name = "xxx")
//    private Collection<Movie> movies = new ArrayList<>();

    public Actor() {}

    public Actor(final String name, final String surname, final String nationality) {
        this.name = name;
        this.surname = surname;
        this.nationality = nationality;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

//    public Collection<Movie> getMovies() {
//        return movies;
//    }
//
//    public void setMovies(Collection<Movie> movies) {
//        this.movies = movies;
//    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return name + " " + surname + " " + nationality;
    }
}
