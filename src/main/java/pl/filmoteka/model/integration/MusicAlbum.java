package pl.filmoteka.model.integration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Class holding information about a single music album from external API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MusicAlbum {

    private String title;

    private String desktopApplicationLink;

    private String websiteApplicationLink;

    private String imageUrl;

    private String artistName;

    private String artistLink;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesktopApplicationLink() {
        return desktopApplicationLink;
    }

    public void setDesktopApplicationLink(String desktopApplicationLink) {
        this.desktopApplicationLink = desktopApplicationLink;
    }

    public String getWebsiteApplicationLink() {
        return websiteApplicationLink;
    }

    public void setWebsiteApplicationLink(String websiteApplicationLink) {
        this.websiteApplicationLink = websiteApplicationLink;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistLink() {
        return artistLink;
    }

    public void setArtistLink(String artistLink) {
        this.artistLink = artistLink;
    }
}
