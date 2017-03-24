package uni.service;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import uni.data.Fetcher;
import uni.data.FetcherThread;
import uni.data.NewsDAO;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

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
        // Enable CORS headers
        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        NewsDAO newsDao = new NewsDAO(configuration.getJdbcConnectionUrl());
        environment.jersey().register(new NewsResource(newsDao));
        new FetcherThread(new Fetcher(newsDao), configuration.getCheckInterval()).start();
    }
}
