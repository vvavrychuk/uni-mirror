package uni.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class UniServiceConfiguration extends Configuration {
    @NotEmpty
    private String jdbcConnectionUrl;

    @NotEmpty
    private String jdbcConnectionUser;

    @NotEmpty
    private String jdbcConnectionPassword;

    private int checkInterval;

    @JsonProperty
    public String getJdbcConnectionUrl() {
        return jdbcConnectionUrl;
    }

    @JsonProperty
    public void setJdbcConnectionUrl(String jdbcConnectionUrl) {
        this.jdbcConnectionUrl = jdbcConnectionUrl;
    }

    @JsonProperty
    public String getJdbcConnectionUser() {
        return jdbcConnectionUser;
    }

    @JsonProperty
    public void setJdbcConnectionUser(String jdbcConnectionUser) {
        this.jdbcConnectionUser = jdbcConnectionUser;
    }

    @JsonProperty
    public String getJdbcConnectionPassword() {
        return jdbcConnectionPassword;
    }

    @JsonProperty
    public void setJdbcConnectionPassword(String jdbcConnectionPassword) {
        this.jdbcConnectionPassword = jdbcConnectionPassword;
    }

    @JsonProperty
    public int getCheckInterval() {
        return checkInterval;
    }

    @JsonProperty
    public void setCheckInterval(int checkInterval) {
        this.checkInterval = checkInterval;
    }
}
