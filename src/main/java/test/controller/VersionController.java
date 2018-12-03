package test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersionController {
    private final static String APP_VERSION = "1.0";

    @GetMapping(value = "version")
    public String getVersion() {
        return APP_VERSION;
    }
}
