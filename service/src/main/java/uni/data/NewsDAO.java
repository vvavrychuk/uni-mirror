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

    public NewsDAO(String connectionUrl) {
        try {
            connection = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        try {
            Statement stmt = connection.createStatement();
            try {
                stmt.executeUpdate("INSERT INTO news(url, title) VALUES ('" +
                        news.getUrl() + "', '" + news.getTitle() + "')");
                return true;
            } catch (PSQLException error) {
                if ((error.getServerErrorMessage().getConstraint() != null) &&
                        (error.getServerErrorMessage().getConstraint().equals("url")))
                    return false;
                else
                    throw error;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
