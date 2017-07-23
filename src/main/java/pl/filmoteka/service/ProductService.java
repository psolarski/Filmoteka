package pl.filmoteka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.exception.InvalidExternalApiResponseException;
import pl.filmoteka.exception.InvalidResourceRequestedException;
import pl.filmoteka.model.Movie;
import pl.filmoteka.model.integration.ProductList;
import pl.filmoteka.repository.MovieRepository;
import pl.filmoteka.repository.ProductRepository;

/**
 * Service class for product.
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MovieRepository movieRepository;

    /**
     * Find all products on Ebay that are somehow related to movie with given ID.
     *
     * @param movieId ID of chosen movie
     * @return Product list object of related products from Ebay
     * @throws InvalidApplicationConfigurationException Invalid configuration for external API
     * @throws InvalidResourceRequestedException Movie with chosen id doesn't exist
     * @throws InvalidExternalApiResponseException Malformed response from Ebay API
     */
    public ProductList findProductsByMovie(Long movieId)
            throws InvalidApplicationConfigurationException, InvalidResourceRequestedException,
            InvalidExternalApiResponseException {

        Movie movie = movieRepository.findOne(movieId);

        if (movie == null) {
            throw new InvalidResourceRequestedException("There's no movie with id " + movieId);
        }

        return productRepository.findProductsByKeywords(movie.getName());
    }
}
