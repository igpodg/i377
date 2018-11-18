package test.conf;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@Import(DbConfig.class)
@ComponentScan(basePackages = {"test.controller", "test.validation"})
public class MvcConfig extends WebMvcConfigurerAdapter {
    //
}
