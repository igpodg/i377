package test;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

@WebListener
public class DatabaseContextListener implements ServletContextListener {
    public final static String DATABASE_URL = "jdbc:hsqldb:mem:db";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Context initialized.");
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream schemaStream = classLoader.getResourceAsStream("schema.sql");
        if (schemaStream == null)
            throw new RuntimeException("No schema file found!");
        String schemaString = new BufferedReader(new InputStreamReader(schemaStream))
                .lines()
                .collect(Collectors.joining("\n"));

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(schemaString);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Context destroyed.");
    }
}
