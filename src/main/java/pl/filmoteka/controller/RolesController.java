package pl.filmoteka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.filmoteka.model.Role;
import pl.filmoteka.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("api/v1/roles/")
public class RolesController {

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public List<Role> findAll() {
        return roleService.findAllRoles();
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Role createRole(@RequestBody Role role) {
        return roleService.createNewRole(role);
    }
}
