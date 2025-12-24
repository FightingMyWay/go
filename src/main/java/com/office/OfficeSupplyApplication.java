package com.office;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.office.mapper")
public class OfficeSupplyApplication {
    public static void main(String[] args) {
        SpringApplication.run(OfficeSupplyApplication.class, args);
    }
}
