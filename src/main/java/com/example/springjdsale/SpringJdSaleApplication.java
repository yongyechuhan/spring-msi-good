package com.example.springjdsale;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SpringJdSaleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringJdSaleApplication.class, args);
        log.debug("test logs");
    }

}
