package pl.filmoteka.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.filmoteka.model.Role;
import pl.filmoteka.model.User;
import pl.filmoteka.repository.RoleRepository;
import pl.filmoteka.repository.UserRepository;
import pl.filmoteka.util.PasswordEncoderProvider;

@Component
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoderProvider passwordEncoderProvider;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (alreadySetup)
            return;

        Role adminRole = createRoleIfNotFound("ADMIN");
        Role userRole = createRoleIfNotFound("USER");

        roleRepository.saveAndFlush(adminRole);
        roleRepository.saveAndFlush(userRole);

        String password = passwordEncoderProvider.getEncoder().encode("password");
        User adminUser = new User("admin", password, "admin@admin.admin");
        adminUser.assignRole(adminRole);
        adminUser.assignRole(userRole);

        User normalUser = new User("user", password, "user@user.user");
        normalUser.assignRole(userRole);

        userRepository.saveAndFlush(adminUser);
        userRepository.saveAndFlush(normalUser);

        alreadySetup = true;
    }

    @Transactional
    private Role createRoleIfNotFound(String name) {
        Role role = roleRepository.findByName(name);

        if (role == null) {
            role = new Role(name);
            role = roleRepository.saveAndFlush(role);
        }

        return role;
    }
}
