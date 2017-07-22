package pl.filmoteka.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
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

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/v1/actors/all").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/actors/name").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/actors/surname").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/actors/nameorsurname").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/actors/create").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/v1/actors/delete").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/v1/actors/update/**").hasRole("ADMIN")

                .antMatchers(HttpMethod.GET, "/api/v1/directors/all").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/directors/name").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/directors/surname").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/directors/nameorsurname").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/directors/create").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/v1/directors/delete").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/v1/directors/update/**").hasRole("ADMIN")

                .antMatchers(HttpMethod.GET, "/api/v1/users/all").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.GET, "/api/v1/users/username").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.GET, "/api/v1/users/email").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.POST, "/api/v1/users/create").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/v1/users/delete").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/v1/users/update/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/v1/users/**/assignrole").hasRole("ADMIN")

                .antMatchers(HttpMethod.GET, "/api/v1/roles/all").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/roles/name/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/v1/roles/create").hasRole("ADMIN")

                .antMatchers(HttpMethod.GET, "/api/v1/movies/all").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/movies/name").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/movies/genre").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/v1/movies/**/reviews").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/movies/create").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/v1/movies/delete").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/v1/movies/update/**").hasRole("ADMIN")
                .anyRequest().authenticated()
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

    @Override
    public void configure(WebSecurity webSecurity) throws Exception
    {
        webSecurity
                .ignoring()
                .antMatchers(HttpMethod.POST, "/api/v1/users/create");
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoderProvider.getEncoder());

        return authProvider;
    }
}