package uni.data;

import org.postgresql.util.PSQLException;
import uni.service.News;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NewsDAO {
    private Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public NewsDAO(Connection connection) {
        this.connection = connection;
    }

    public List<News> getAll() {
        List<News> news = new ArrayList<News>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM news");
            while (rs.next()) {
                news.add(new News(
                        rs.getLong("id"),
                        rs.getString("url"),
                        rs.getString("title")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return news;
    }

    public boolean addIfNotExists(News news) {
        try (Statement stmt = connection.createStatement()) {
            return stmt.executeUpdate("INSERT INTO news(url, title) VALUES ('" +
                    news.getUrl() + "', '" + news.getTitle() + "') ON CONFLICT(url) DO NOTHING") > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
