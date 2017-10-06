package com.serhiikutsyi.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Serhii Kutsyi
 */
@EnableScheduling
@SpringBootApplication
public class ProducerApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ProducerApplication.class, args);
    }

}


