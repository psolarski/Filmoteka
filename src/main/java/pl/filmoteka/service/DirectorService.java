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
    public List<Director> findAllDirectors() {
        return directorRepository.findAll();
    }

    @Transactional
    public Director addNewDirector(Director director) {
        return directorRepository.saveAndFlush(director);
    }

    @Transactional
    public void deleteDirector(Long id) {
        directorRepository.delete(id);
    }
}
