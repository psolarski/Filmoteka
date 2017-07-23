package pl.filmoteka.repository;

import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.exception.InvalidExternalApiResponseException;
import pl.filmoteka.model.integration.ProductList;

/**
 * Custom repository for product from Ebay.
 */
public interface ProductRepository {

    public ProductList findProductsByKeywords(String keywords) throws InvalidApplicationConfigurationException, InvalidExternalApiResponseException;
}
