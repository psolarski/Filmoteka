package pl.filmoteka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Class responsible for basic security
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "api/v1/actors/").hasAnyRole("guest", "admin", "user")
                .antMatchers(HttpMethod.POST, "api/v1/actors/create").hasAnyRole("admin", "user")
                .antMatchers(HttpMethod.DELETE, "api/v1/actors/delete").hasRole("admin")
                .antMatchers(HttpMethod.PUT, "api/v1/actors/update").hasAnyRole("admin", "user")
                .anyRequest().authenticated()
                .and().httpBasic()
                .and().csrf().disable()
                .headers().frameOptions().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("password").roles("user");
        auth.inMemoryAuthentication().withUser("admin").password("password").roles("admin");
        auth.inMemoryAuthentication().withUser("guest").password("password").roles("guest");
    }
}