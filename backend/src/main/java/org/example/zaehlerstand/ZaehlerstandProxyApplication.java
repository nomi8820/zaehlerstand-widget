package org.example.zaehlerstand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Startet den Proxy zwischen Widget und Mock-API. */
@SpringBootApplication
public class ZaehlerstandProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZaehlerstandProxyApplication.class, args);
    }

}
