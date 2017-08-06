package pl.filmoteka.repository.integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.filmoteka.exception.InvalidApplicationConfigurationException;
import pl.filmoteka.exception.InvalidExternalApiResponseException;
import pl.filmoteka.model.integration.Product;
import pl.filmoteka.model.integration.ProductList;
import pl.filmoteka.repository.ProductRepository;
import pl.filmoteka.repository.ProductRepositoryImpl;
import pl.filmoteka.util.EbaySearchResponseMapper;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ProductRepositoryTests {

    @Mock
    private RestTemplate mockedRestTemplate;

    @Mock
    private EbaySearchResponseMapper mapper;

    @InjectMocks
    private ProductRepository sut = new ProductRepositoryImpl();

    private String fakeKeywords = "fake+keywords";

    @Before
    public void init() {
        mockApiUrl("fakeApiUrl");
        ReflectionTestUtils.setField(sut, "ebayPerPage", "5");
        ReflectionTestUtils.setField(sut, "ebaySecurityAppname", "fakeAppName");
    }

    private void mockApiUrl(String url) {
        ReflectionTestUtils.setField(sut, "ebayApiUrl", url);
    }

    @Test
    public void emptyProductListIfNoProductsWereFound()
            throws InvalidExternalApiResponseException, InvalidApplicationConfigurationException {

        Mockito.when(mockedRestTemplate.getForObject(
                Matchers.any(URI.class), Matchers.eq(String.class))
        ).thenReturn("");
        Mockito.when(mapper.from(Matchers.anyString())).thenReturn(new ProductList(new ArrayList<>()));

        assertThat(sut.findProductsByKeywords(fakeKeywords)).isNotNull();
        assertThat(sut.findProductsByKeywords(fakeKeywords).getProducts()).isNotNull().isEmpty();
    }

    @Test
    public void singleItemInProductListIfOneProductWasFound()
            throws InvalidExternalApiResponseException, InvalidApplicationConfigurationException {

        Mockito.when(mockedRestTemplate.getForObject(
                Matchers.any(URI.class), Matchers.eq(String.class))
        ).thenReturn("");
        Mockito.when(mapper.from(Matchers.anyString()))
                .thenReturn(new ProductList(new ArrayList<>(Collections.singletonList(new Product()))));

        assertThat(sut.findProductsByKeywords(fakeKeywords)).isNotNull();
        assertThat(sut.findProductsByKeywords(fakeKeywords).getProducts()).isNotNull().hasSize(1);
    }

    @Test
    public void threeItemsInProductListIfThreeProductsWereFound()
            throws InvalidExternalApiResponseException, InvalidApplicationConfigurationException {

        Mockito.when(mockedRestTemplate.getForObject(
                Matchers.any(URI.class), Matchers.eq(String.class))
        ).thenReturn("");
        Mockito.when(mapper.from(Matchers.anyString()))
                .thenReturn(new ProductList(new ArrayList<>(Arrays.asList(new Product(), new Product(), new Product()))));

        assertThat(sut.findProductsByKeywords(fakeKeywords)).isNotNull();
        assertThat(sut.findProductsByKeywords(fakeKeywords).getProducts()).isNotNull().hasSize(3);
    }

    @Test(expected = InvalidApplicationConfigurationException.class)
    public void failIfInvalidUrlToSearchProducts()
            throws InvalidExternalApiResponseException, InvalidApplicationConfigurationException {
        mockApiUrl(" aąśćżź // \\");

        sut.findProductsByKeywords(fakeKeywords);
    }

    @Test(expected = InvalidApplicationConfigurationException.class)
    public void failIfConnectionErrorWhileSearchingForProducts()
            throws InvalidExternalApiResponseException, InvalidApplicationConfigurationException {
        Mockito.when(mockedRestTemplate.getForObject(
                Matchers.any(URI.class), Matchers.eq(String.class))
        ).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        sut.findProductsByKeywords(fakeKeywords);
    }

    @Test(expected = InvalidApplicationConfigurationException.class)
    public void failIfInvalidCharacterEncodingForUriBuilder()
            throws InvalidExternalApiResponseException, InvalidApplicationConfigurationException {
        ReflectionTestUtils.setField(sut, "characterEncoding", "fakeEncoding");

        sut.findProductsByKeywords(fakeKeywords);
    }
}
