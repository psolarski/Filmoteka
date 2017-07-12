package pl.filmoteka.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Email;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity that represents an user.
 */
@Entity
@Table(name = "user")
@NamedEntityGraph(name = "graph.User.movies",
        attributeNodes = @NamedAttributeNode("movies"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", unique = true, nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Email
    private String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Notification> notifications = new HashSet<>(0);

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Role.class, cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id",
                                  nullable = false, updatable = false),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id",
                                         nullable = false, updatable = false)
    )
    private Set<Role> roles = new HashSet<>();

    // List of movies that the user has watched
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Movie.class, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "watched_movies",
            joinColumns ={@JoinColumn(name = "user_id", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "movie_id", nullable = false, updatable = false)}
    )
    private Set<Movie> movies = new HashSet<>();

    public User() {
    }

    public User(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }

    public int hashCode() {
        return new HashCodeBuilder().append(id)
                .append(login)
                .append(email)
                .toHashCode();
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
