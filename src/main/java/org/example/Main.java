package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RESTfulProxyService {
    public static void main(String[] args) {
        SpringApplication.run(RESTfulProxyService.class, args);
    }
}
