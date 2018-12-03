package test.initialization;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DataSourceInitializer {
    private static DriverManagerDataSource dataSource = null;

    public static DriverManagerDataSource getDataSource(Environment env) {
        if (dataSource == null) {
            dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
            dataSource.setUrl(env.getProperty("db.url"));
        }
        return dataSource;
    }
}
