package pl.filmoteka.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JSON custom configuration
 */
@Configuration
public class JacksonConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);

        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, true);

        return mapper;
    }
}
