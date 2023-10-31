package com.zsh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@MapperScan("com.zsh.mapper")
public class HabitDeveloperApplication {

    public static void main(String[] args) {
        SpringApplication.run(HabitDeveloperApplication.class, args);
    }

}
