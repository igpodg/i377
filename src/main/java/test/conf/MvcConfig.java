package test.conf;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
//@Import(DbConfig.class)
@ComponentScan(basePackages = {"test.controller", "test.validation", "test.initialization"})
public class MvcConfig {
    //
}
