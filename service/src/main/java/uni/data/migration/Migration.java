package uni.data.migration;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

public class Migration implements AutoCloseable {
  private static final Logger log = Logger.getLogger(Migration.class.getName());
  private static final int VERSION_UNDEFINED = 0;
  private static final String SYSTEM_DB_NAME = "postgres";

  private Connection conn;

  private List<String> listResources(Class cls, String path) throws IOException {
    InputStream directory = cls.getResourceAsStream(path);
    if (directory != null) {
      List<String> resources = IOUtils.readLines(directory, Charset.defaultCharset());
      directory.close();
      return resources;
    } else {
      path = Paths.get(cls.getPackage().getName().replace('.', '/'), path).normalize().toString().
        replace(File.separatorChar, '/');

      JarFile jarFile = new JarFile(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
      Enumeration<JarEntry> jarEntries = jarFile.entries();
      List<String> resources = new ArrayList<>();
      while (jarEntries.hasMoreElements()) {
        String jarPath = jarEntries.nextElement().getName();
        if (!jarPath.startsWith(path))
          continue;

        String resource = jarPath.substring(path.length());
        if (resource.startsWith("/"))
          resource = resource.substring(1);
        resources.add(resource);
      }
      return resources;
    }
  }

  private List<String> getScripts() throws IOException {
    SortedMap<Integer, String> scripts = new TreeMap<>();

    List<String> resources = listResources(getClass(), ".");
    resources.stream().filter(r -> r.endsWith(".sql")).forEach(
      r -> {
        Integer num = Integer.parseInt(r.substring(0, r.indexOf('_')));
        scripts.put(num, r);
      }
    );

    int max = Collections.max(scripts.keySet());
    for (int i = 1; i <= max; i++) {
      if (!scripts.keySet().contains(i))
        throw new RuntimeException("Missing migration script number " + i);
    }

    List<String> scriptsList = new ArrayList<>();
    scriptsList.add(null);
    scriptsList.addAll(scripts.values());
    return scriptsList;
  }

  private void createDatabase(String url, String user, String password) {
    log.info("Creating database...");
    String name = url.substring(url.lastIndexOf(':') + 1);
    url = url.substring(0, url.lastIndexOf(':'));

    try (Connection conn = DriverManager.getConnection(url + ":" + SYSTEM_DB_NAME, user, password);
         Statement stmt = conn.createStatement()) {
      stmt.executeUpdate("CREATE database " + name);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Migration(String url, String user, String password) {
    try {
      conn = DriverManager.getConnection(url, user, password);
    } catch (SQLException e) {
      createDatabase(url, user, password);
    }

    try {
      conn = DriverManager.getConnection(url, user, password);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void migrate() throws IOException, SQLException {
    List<String> scripts = getScripts();
    int version = getVersion();
    for (int i = version + 1; i < scripts.size(); i++) {
      String name = scripts.get(i);
      log.info("Executing script " + name);

      try (InputStream scriptStream = getClass().getResourceAsStream(name);
           Statement stmt = conn.createStatement()) {
        String script = IOUtils.toString(scriptStream, Charset.defaultCharset());
        stmt.executeUpdate(script);
      }
    }
    log.info("Migration finished!");
  }

  @Override
  public void close() {
    try {
      conn.close();
    } catch (SQLException e) {
    }
  }

  public int getVersion() {
    try (Statement stmt = conn.createStatement()) {
      ResultSet rs = stmt.executeQuery("SELECT EXISTS(SELECT *\n" +
              "  FROM information_schema.tables\n" +
              "  WHERE (table_schema = 'public') AND (table_name = 'version'))");
      rs.next();
      if (!rs.getBoolean(1))
        return VERSION_UNDEFINED;

      rs = stmt.executeQuery("select version from version");
      if (!rs.next())
        return VERSION_UNDEFINED;
      return rs.getInt(1);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
