package org.opensourceway.uvp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ImportResource(locations = {"classpath:import-vuln.xml"})
@EnableTransactionManagement
@EnableRetry
public class UvpApplication extends SpringBootServletInitializer {

    private static final Logger logger = LoggerFactory.getLogger(UvpApplication.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(UvpApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(UvpApplication.class, args);
        logger.info("UVP service has started");
    }
}