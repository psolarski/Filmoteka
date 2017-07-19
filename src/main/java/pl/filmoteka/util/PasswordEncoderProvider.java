package pl.filmoteka.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderProvider {

    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder(11);
    }
}
