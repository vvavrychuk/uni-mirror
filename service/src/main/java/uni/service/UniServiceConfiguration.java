package uni.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class UniServiceConfiguration extends Configuration {
    @NotEmpty
    private String jdbcConnectionUrl;

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
    public int getCheckInterval() {
        return checkInterval;
    }

    @JsonProperty
    public void setCheckInterval(int checkInterval) {
        this.checkInterval = checkInterval;
    }
}
