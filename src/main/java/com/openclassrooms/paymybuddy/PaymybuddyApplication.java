package com.openclassrooms.paymybuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class PaymybuddyApplication {

    public static void main(String[] args) {

        SpringApplication.run(PaymybuddyApplication.class, args);
    }
}


