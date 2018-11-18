package test.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Configuration
@PropertySource("classpath:/application.properties")
@ComponentScan(basePackages = {"test.dao"})
public class DbConfig {
    @Autowired
    public Environment env;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.hsqldb.jdbcDriver");
        ds.setUrl(env.getProperty("db.url"));

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream schemaStream = classLoader.getResourceAsStream("schema.sql");
        if (schemaStream == null)
            throw new RuntimeException("No schema file found!");
        String schemaString = new BufferedReader(new InputStreamReader(schemaStream))
                .lines()
                .collect(Collectors.joining("\n"));

        new JdbcTemplate(ds).update(schemaString);

        return ds;
    }

    @Bean
    public JdbcTemplate getTemplate() {
        return new JdbcTemplate(dataSource());
    }
}
