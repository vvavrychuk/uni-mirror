package uni.service;

import io.dropwizard.Application;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import uni.data.Fetcher;
import uni.data.FetcherThread;
import uni.data.NewsDAO;
import uni.data.migration.Migration;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.EnumSet;

public class UniServiceApplication extends Application<UniServiceConfiguration> {
    private void configureCors(Environment environment) {
        // Enable CORS headers
        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }

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
        configureCors(environment);

        try (Migration migration = new Migration(
                configuration.getDataSourceFactory().getUrl(),
                configuration.getDataSourceFactory().getUser(),
                configuration.getDataSourceFactory().getPassword())) {
            migration.migrate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            ManagedDataSource dataSource = configuration.getDataSourceFactory().build(environment.metrics(), "postgresql");
            NewsDAO newsDao = new NewsDAO(dataSource.getConnection());
            environment.jersey().register(new NewsResource(newsDao));
            new FetcherThread(new Fetcher(newsDao), configuration.getCheckInterval()).start();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
