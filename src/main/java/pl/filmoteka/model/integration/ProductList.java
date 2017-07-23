package pl.filmoteka.model.integration;

import java.util.ArrayList;
import java.util.List;

/**
 * Class holding information about a single product list from external API. Added as a wrapper for product list
 * because results url was needed.
 */
public class ProductList {

    private List<Product> products = new ArrayList<>();

    private String resultsUrl;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getResultsUrl() {
        return resultsUrl;
    }

    public void setResultsUrl(String resultsUrl) {
        this.resultsUrl = resultsUrl;
    }
}
