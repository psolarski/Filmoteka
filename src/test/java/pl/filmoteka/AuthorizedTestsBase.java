package pl.filmoteka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import javax.annotation.PostConstruct;

public abstract class AuthorizedTestsBase {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    protected TestRestTemplate testRestTemplateAsUser;
    protected TestRestTemplate testRestTemplateAsAdmin;

    protected String normalUserUsername = "user";
    protected String normalUserPassword = "password";
    protected String adminUserUsername = "admin";
    protected String adminUserPassword = "password";

    @PostConstruct
    public void initAuthoriedTemplates() {
        testRestTemplateAsUser = testRestTemplate.withBasicAuth(normalUserUsername, normalUserPassword);
        testRestTemplateAsAdmin = testRestTemplate.withBasicAuth(adminUserUsername, adminUserPassword);
    }
}