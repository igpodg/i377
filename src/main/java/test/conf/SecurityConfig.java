package test.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import test.initialization.DataSourceInitializer;
import test.security.ApiAuthenticationFilter;
import test.security.handlers.*;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    public Environment env;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        // other configurations
        http.authorizeRequests()
                .antMatchers("/api/version").permitAll()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/logout").permitAll()
                .antMatchers("/api/**").hasAnyRole("USER", "ADMIN");

        //http.formLogin();

        http.exceptionHandling().authenticationEntryPoint(new ApiEntryPoint());
        http.exceptionHandling().accessDeniedHandler(new ApiAccessDeniedHandler());

        http.logout()
                .logoutUrl("/api/logout")
                .logoutSuccessHandler(new ApiLogoutSuccessHandler());

        http.addFilterAfter(apiLoginFilter("/api/login"), LogoutFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        //DriverManagerDataSource dataSource = new DriverManagerDataSource();
        //dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        //dataSource.setUrl(env.getProperty("db.url"));
        DriverManagerDataSource dataSource = DataSourceInitializer.getDataSource(null);

        // configure user and password info
        builder.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(getUserQuery())
                .authoritiesByUsernameQuery(getAuthoritiesQuery());
    }

    public Filter apiLoginFilter(String url) throws Exception {
        ApiAuthenticationFilter filter = new ApiAuthenticationFilter(url);

        filter.setAuthenticationManager(authenticationManager());

        // add success and failure handlers
        filter.setAuthenticationSuccessHandler(new ApiAuthSuccessHandler());
        filter.setAuthenticationFailureHandler(new ApiAuthFailureHandler());

        return filter;
    }

    private String getUserQuery() {
        return "SELECT username, password, enabled FROM users WHERE username = ?";
    }

    private String getAuthoritiesQuery() {
        return "SELECT username, authority FROM authorities WHERE username = ?";
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}