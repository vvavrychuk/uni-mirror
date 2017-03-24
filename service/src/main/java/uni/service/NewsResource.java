package uni.service;

import com.codahale.metrics.annotation.Timed;
import uni.data.NewsDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/news")
@Produces(MediaType.APPLICATION_JSON)
public class NewsResource {
    private NewsDAO newsDao;

    public NewsResource(NewsDAO newsDao) {
        this.newsDao = newsDao;
    }

    @GET
    @Timed
    public List<News> getNews() {
        return newsDao.getAll();
    }
}
