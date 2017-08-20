package pl.filmoteka.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import pl.filmoteka.util.PasswordEncoderProvider;

/**
 * Class responsible for basic security.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoderProvider passwordEncoderProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // All routes also available for guests (like get all movies) are not included in
        // below declarations because permitAll() is only for authorized users (permitAll != also for guests)
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/v1/actors/create").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/v1/actors/delete").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/v1/actors/movie").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/v1/actors/update/**").hasRole("ADMIN")

                .antMatchers(HttpMethod.POST, "/api/v1/directors/create").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/v1/directors/delete").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/v1/directors/update/**").hasRole("ADMIN")

                .antMatchers(HttpMethod.GET, "/api/v1/users/all").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.GET, "/api/v1/users/username").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.GET, "/api/v1/users/email").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.DELETE, "/api/v1/users/delete").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/v1/users/update/**").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.POST, "/api/v1/users/**/assignrole").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/users/similarity/**").hasRole("USER")

                .antMatchers(HttpMethod.GET, "/api/v1/roles/all").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/roles/name/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/v1/roles/create").hasRole("ADMIN")

                .antMatchers(HttpMethod.POST, "/api/v1/movies/create").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/v1/movies/delete").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/v1/movies/update/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/movies/recommendations").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/api/v1/movies/**/watched").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/api/v1/movies/**/assign/actor/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/movies/**/assign/director/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/movies/movieFromMovieDb/**").hasAnyRole("ADMIN", "USER")

                .antMatchers(HttpMethod.POST, "/api/v1/ratings/create").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.DELETE, "/api/v1/ratings/delete/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/v1/ratings/update/**").hasRole("ADMIN")
//                .anyRequest().authenticated()
                .and().httpBasic()
                .and().csrf().disable()
                .headers().frameOptions().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoderProvider.getEncoder());

        return authProvider;
    }
}