package org.hkijena.olr;

import org.hkijena.olr.config.AccountConfig;
import org.hkijena.olr.config.JwtConfig;
import org.hkijena.olr.config.RuntimeConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({RuntimeConfig.class, AccountConfig.class, JwtConfig.class})
public class OLRWebApplicationServer {

    public OLRWebApplicationServer() {
    }

    public static void main(String[] args) {
        SpringApplication.run(OLRWebApplicationServer.class, args);
    }
}
