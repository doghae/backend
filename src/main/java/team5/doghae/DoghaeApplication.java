package team5.doghae;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DoghaeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoghaeApplication.class, args);
    }

}
