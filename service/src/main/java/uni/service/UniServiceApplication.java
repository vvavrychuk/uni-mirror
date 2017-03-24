package uni.service;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import uni.data.NewsDAO;

public class UniServiceApplication extends Application<UniServiceConfiguration> {
    public static void main(String[] args) throws Exception {
        new UniServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "uni-service";
    }

    @Override
    public void initialize(Bootstrap<UniServiceConfiguration> bootstrap) {
    }

    @Override
    public void run(UniServiceConfiguration configuration,
                    Environment environment) {
        environment.jersey().register(new NewsResource(new NewsDAO(configuration.getJdbcConnectionUrl())));
    }
}
