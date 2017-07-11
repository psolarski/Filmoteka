package pl.filmoteka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.filmoteka.model.Director;
import pl.filmoteka.repository.DirectorRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Piotr on 14.04.2017.
 */
@Service
public class DirectorService {

    @Autowired
    DirectorRepository directorRepository;

    @Transactional
    public Director find(Long id) {
        return directorRepository.findOne(id);
    }

    @Transactional
    public List<Director> findAllDirectors() {
        return directorRepository.findAll();
    }

    @Transactional
    public List<Director> findByName(String name) {
        return directorRepository.findByName(name);
    }

    @Transactional
    public List<Director> findBySurname(String surname) {
        return directorRepository.findBySurname(surname);
    }

    @Transactional
    public List<Director> findByNameOrSurname(String name, String surname) {
        return directorRepository.findByNameOrSurname(name, surname);
    }

    @Transactional
    public Director addNewDirector(Director director) {
        return directorRepository.saveAndFlush(director);
    }

    @Transactional
    public void deleteDirector(Long id) {
        directorRepository.delete(id);
    }

    @Transactional
    public Director updateDirector(Director director) {
        return directorRepository.saveAndFlush(director);
    }
}
