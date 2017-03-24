package uni.service;

import com.fasterxml.jackson.annotation.JsonProperty;

public class News {
    private long id;

    private String title;

    public News(long id, String title) {
        this.id = id;
        this.title = title;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty
    public String getTitle() {
        return title;
    }
}
