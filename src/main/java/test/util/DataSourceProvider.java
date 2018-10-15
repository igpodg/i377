package test.util;

import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;

public class DataSourceProvider {
    public final static String DATABASE_URL = "jdbc:hsqldb:mem:db";
    private static BasicDataSource dataSource = null;

    public static DataSource getDataSource() {
        if (dataSource != null)
            return dataSource;

        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSource.setUrl(DATABASE_URL);
        dataSource.setMaxActive(3);
        return dataSource;
    }
}
