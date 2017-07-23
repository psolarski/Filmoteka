package pl.filmoteka.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pl.filmoteka.exception.InvalidExternalApiResponseException;
import pl.filmoteka.model.integration.Product;
import pl.filmoteka.model.integration.ProductList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper utility to map response from Ebay API to desirable structure represented by ProductList.
 */
@Component
public class EbaySearchResponseMapper {

    /**
     * Map product list from given json response from Ebay API.
     *
     * @param responseJson Response from Ebay API as normal string
     * @return Processed product list
     * @throws InvalidExternalApiResponseException Malformed response from Ebay API
     */
    public ProductList from(String responseJson) throws InvalidExternalApiResponseException {
        ObjectMapper mapper = new ObjectMapper();
        List<Product> products = new ArrayList<>();
        ProductList list = new ProductList();

        try {
            JsonNode node = mapper.readTree(responseJson);

            // Get number of found products on Ebay
            Integer noOfResults = node.get("findItemsByKeywordsResponse").get(0)
                    .get("searchResult").get(0).get("@count").asInt();

            if (noOfResults > 0) {
                products = readProductsFromJsonResponse(node);
            }

            // Get link to search results page
            String resultsUrl = node.get("findItemsByKeywordsResponse").get(0).get("itemSearchURL").get(0).asText();
            list.setProducts(products);
            list.setResultsUrl(resultsUrl);

        } catch (IOException e) {
            throw new InvalidExternalApiResponseException("Response from Ebay API was malformed!", e);
        }

        return list;
    }

    /**
     * Read products received from Ebay API as JSON.
     *
     * @param rootNode Root node of received JSON
     * @return List of products
     */
    private List<Product> readProductsFromJsonResponse(JsonNode rootNode) {
        List<Product> products = new ArrayList<>();

        rootNode.get("findItemsByKeywordsResponse").get(0).get("searchResult").get(0).get("item")
                .forEach(p -> {
                    Product product = new Product();
                    product.setTitle(p.get("title").get(0).asText());

                    JsonNode categoryName = p.get("primaryCategory").get(0).get("categoryName").get(0);
                    if (categoryName != null) {
                        product.setCategoryName(categoryName.asText());
                    }

                    JsonNode galleryUrl = p.get("galleryURL");
                    if (galleryUrl != null && galleryUrl.get(0) != null) {
                        product.setGalleryUrl(galleryUrl.get(0).asText());
                    }

                    JsonNode viewItemUrl = p.get("viewItemURL");
                    if (viewItemUrl != null && viewItemUrl.get(0) != null) {
                        product.setViewItemUrl(viewItemUrl.get(0).asText());
                    }

                    JsonNode location = p.get("location");
                    if (location != null && location.get(0) != null) {
                        product.setLocation(location.get(0).asText());
                    }

                    products.add(product);
                });

        return products;
    }
}
