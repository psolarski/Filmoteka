package pl.filmoteka.repository;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.exception.InvalidExternalApiResponseException;
import pl.filmoteka.model.integration.ProductList;
import pl.filmoteka.util.EbaySearchResponseMapper;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.logging.Logger;

/**
 * Custom repository implementation for product from Ebay.
 */
@Repository
public class ProductRepositoryImpl implements ProductRepository {

    // Logger
    final static Logger logger = Logger.getLogger(ProductRepositoryImpl.class.getName());

    @Value("${api.ebay.securityappname}")
    private String ebaySecurityAppname;

    @Value("${api.url.ebay.search}")
    private String ebayApiUrl;

    @Value("${api.misc.ebay.perpage}")
    private String ebayPerPage;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EbaySearchResponseMapper mapper;

    private String characterEncoding = "UTF-8";

    /**
     * Find products on Ebay by keywords.
     *
     * @param keywords Keywords to search for as a string
     * @return List of found products on Ebay
     * @throws InvalidApplicationConfigurationException Invalid configuration for external API
     * @throws InvalidExternalApiResponseException Malformed response from Ebay API
     */
    @Override
    public ProductList findProductsByKeywords(String keywords)
            throws InvalidApplicationConfigurationException, InvalidExternalApiResponseException {
        String filledUrl = ebayApiUrl;

        try {
            filledUrl = filledUrl
                    .replace("{security_appname}", ebaySecurityAppname)
                    .replace("{keywords}", URLEncoder.encode(keywords, characterEncoding))
                    .replace("{perpage}", ebayPerPage);

            URIBuilder uri = new URIBuilder(filledUrl);
            String rawResponse = restTemplate.getForObject(uri.build(), String.class);

            return mapper.from(rawResponse);

        } catch (URISyntaxException e) {
            logger.severe("Invalid URI");

        } catch (HttpClientErrorException e) {
            logger.severe("Connection error");

        } catch (UnsupportedEncodingException e) {
            logger.severe("Invalid encoding");
        }

        throw new InvalidApplicationConfigurationException();
    }
}
