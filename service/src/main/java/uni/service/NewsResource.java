package uni.service;

import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

@Path("/news")
@Produces(MediaType.APPLICATION_JSON)
public class NewsResource {
    @GET
    @Timed
    public List<News> getNews() {
        return Arrays.asList(
                new News(1,"foo"),
                new News(2,"bar"));
    }
}
