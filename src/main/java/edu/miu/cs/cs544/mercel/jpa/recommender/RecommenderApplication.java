package edu.miu.cs.cs544.mercel.jpa.recommender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
@EnableFeignClients(basePackages = "edu.miu.cs.cs544.mercel.jpa.recommender.client")
public class RecommenderApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecommenderApplication.class, args);
    }
}