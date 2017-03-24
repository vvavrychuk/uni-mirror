package uni.service;

import com.fasterxml.jackson.annotation.JsonProperty;

public class News {
    private long id;

    private String url;

    private String title;

    public News(long id, String url, String title) {
        this.id = id;
        this.url = url;
        this.title = title;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty
    public String getUrl() {
        return url;
    }

    @JsonProperty
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty
    public String getTitle() {
        return title;
    }
}
