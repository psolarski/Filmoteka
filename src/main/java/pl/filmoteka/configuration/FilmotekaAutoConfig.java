package pl.filmoteka.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by Piotr on 05.04.2017.
 */
@Component
public class FilmotekaAutoConfig {

    @Bean
    @ConfigurationProperties(prefix = "bean.custom.mybean")
    public MyBean createConfiguredBean() {
        return new MyBean();
    }
}
