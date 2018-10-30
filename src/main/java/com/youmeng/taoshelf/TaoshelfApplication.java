package com.youmeng.taoshelf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TaoshelfApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaoshelfApplication.class, args);
    }

}
