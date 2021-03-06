package test.conf;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import test.initialization.DataSourceInitializer;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.stream.Collectors;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:/application.properties")
@ComponentScan(basePackages = {"test.dao"})
public class DbConfig {
    @Autowired
    public Environment env;

    @Bean
    public DataSource dataSource() {
        //DriverManagerDataSource ds = new DriverManagerDataSource();
        //ds.setDriverClassName("org.hsqldb.jdbcDriver");
        //ds.setUrl(env.getProperty("db.url"));
        DriverManagerDataSource ds = DataSourceInitializer.getDataSource(env);

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

    /*@Bean
    public JdbcTemplate getTemplate() {
        return new JdbcTemplate(dataSource());
    }*/

    @Bean
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory entityManagerFactory) {

        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        factory.setPackagesToScan("test.model");
        factory.setDataSource(dataSource());
        factory.setJpaProperties(additionalProperties());
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    private Properties additionalProperties() {
        Properties properties = new Properties();
        //properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.hbm2ddl.auto", "validate");
        properties.setProperty("hibernate.dialect",
                "org.hibernate.dialect.HSQLDialect");
        properties.setProperty("hibernate.show_sql", "false");
        //properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.format_sql", "true");

        return properties;
    }
}
