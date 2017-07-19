package pl.filmoteka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.filmoteka.model.Role;
import pl.filmoteka.repository.RoleRepository;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Transactional
    public Role find(String name) {
        return roleRepository.findByName(name);
    }

    @Transactional
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    @Transactional
    public Role createNewRole(Role role) {
        return roleRepository.saveAndFlush(role);
    }
}
