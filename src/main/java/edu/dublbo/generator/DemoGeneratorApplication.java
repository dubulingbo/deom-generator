package edu.dublbo.generator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan(basePackages = "edu.dublbo.generator")
public class DemoGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoGeneratorApplication.class, args);
    }

}
