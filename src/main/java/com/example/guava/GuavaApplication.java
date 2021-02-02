package com.example.guava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class GuavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuavaApplication.class, args);

    }

}
