package pl.filmoteka.model.integration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class holding information about a single product from external API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

    @JsonProperty("title")
    private String title;

    @JsonProperty("categoryName")
    private String categoryName;

    @JsonProperty("galleryUrl")
    private String galleryUrl;

    @JsonProperty("viewItemUrl")
    private String viewItemUrl;

    @JsonProperty("location")
    private String location;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setGalleryUrl(String galleryUrl) {
        this.galleryUrl = galleryUrl;
    }

    public void setViewItemUrl(String viewItemUrl) {
        this.viewItemUrl = viewItemUrl;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
